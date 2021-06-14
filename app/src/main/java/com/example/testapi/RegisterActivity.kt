package com.example.testapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import com.example.testapi.api.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {
    private val email: String
        get() {
            return initForm(R.id.email).text.toString()
        }

    private val password: String
        get() {
            return initForm(R.id.pwd).text.toString()
        }

    private val cPassword: String
        get() {
            return initForm(R.id.c_pwd).text.toString()
        }

    private val name: String
        get() {
            return initForm(R.id.name).text.toString()
        }

    private val school: String
        get() {
            return initForm(R.id.school).text.toString()
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        findViewById<Button>(R.id.sign_up).setOnClickListener {
            if (!formEmpty())
                performRegistration();
        }
        findViewById<LinearLayout>(R.id.login).setOnClickListener {
            onBackPressed()
        }
    }

    private fun performRegistration() {
        val call = ApiRepository(ApiDao)
            .register(Register(email, password, name, school));

        call.enqueue(object : Callback<LoginResult> {
            override fun onResponse(call: Call<LoginResult>, response: Response<LoginResult>) {
                when {
                    response.isSuccessful -> {
                        val body = response.body()
                        val error = response.body()?.error
                        Log.d("myApi", body.toString());
                        Toast.makeText(applicationContext, body?.message, Toast.LENGTH_SHORT)
                            .show();
                        if (error != true) {
                            val intent = Intent(applicationContext, DashboardActivity::class.java)
//                            intent.putExtra("user", body?.user)
                            startActivity(intent)
                        }
                    }
                    response.code() == 204 -> {
                        Log.d("myApi", "Password not matched");
                    }
                    else -> {
                        Log.e("myApi", "response failed!! onResponse");
                        Log.e("myApi", "error code is ${response.code()}");
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

    private fun initForm(id: Int): EditText = findViewById<EditText>(id)

    private fun formEmpty(): Boolean {
        if (email.isBlank() || name.isBlank() || school.isBlank()) {
            Toast.makeText(
                applicationContext,
                "make sure you fill all the form",
                Toast.LENGTH_SHORT
            ).show()
            return true
        }
        if (password.isBlank() || cPassword.isBlank()) {
            Toast.makeText(
                applicationContext,
                "one of the two password fields is empty",
                Toast.LENGTH_SHORT
            ).show()
            return true;
        }
        if (password != cPassword) {
            Toast.makeText(applicationContext, "password doesn't match", Toast.LENGTH_SHORT).show()
            return true;
        }
        return false
    }
}