package com.example.testapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import com.example.testapi.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class LoginActivity : AppCompatActivity() {

    private val password: String
        get() {
            return findViewById<EditText>(R.id.pwd).text.toString();
        }

    private val email: String
        get() {
            return findViewById<EditText>(R.id.email).text.toString();
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)
        findViewById<LinearLayout>(R.id.sign_up).setOnClickListener {
            this.redirectToRegistration()
        }
        findViewById<Button>(R.id.login).setOnClickListener {
            if (!formEmpty())
                this.login()
            else
                Toast.makeText(
                    applicationContext,
                    "make sure you fill all the form",
                    Toast.LENGTH_SHORT
                ).show()
        }
    }

    private fun redirectToRegistration() {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }


    private fun login() {


        val call = ApiRepository(ApiDao)
            .login(Login(email, password));

        call.enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        val error = response.body()?.error
                        print(body)
                        Log.d("myApi", body.toString());
                        Toast.makeText(applicationContext, body?.message, Toast.LENGTH_SHORT)
                            .show();
                        if (error != true) {
                            val intent =
                                Intent(applicationContext, DashboardActivity::class.java)
//                            intent.putExtra("user", body?.user)
//                            intent.putExtra("id", body?.user?.id);
                            startActivity(intent)
                        }
                    }
                    response.code() == 204 -> {
                        Log.d("myApi", "Password not matched");
                        Toast.makeText(
                            applicationContext,
                            "Password not matched",
                            Toast.LENGTH_SHORT
                        ).show();
                    }
                    else -> {
                        Log.e("myApi", "response failed!! onResponse");
                        Log.e("myApi", "error code is ${response.code()}");
                        Log.e("myApi", "credentials-> $email $password");
                        Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                Log.e("myApi", "response failed!!");
                Toast.makeText(
                    applicationContext,
                    "Some Error occur, please check your internet connection.",
                    Toast.LENGTH_SHORT
                ).show();
            }

        })
    }

    private fun formEmpty(): Boolean = email.isBlank() || password.isBlank()
}