package com.example.testapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import android.widget.Toolbar
import com.example.testapi.api.*
import com.example.testapi.databinding.ActivityProfileBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileActivity : AppCompatActivity() {
    lateinit var binding: ActivityProfileBinding
    lateinit var user: User

    private val tv get() = findViewById<TextView>(R.id.tv)
    private val topic get() = findViewById<TextView>(R.id.title)
    private val toolBar get() = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
    val id get() = intent.extras?.getInt("id")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        val extras = intent.extras
        val serializable = extras?.getSerializable("user")
        toolBar.setOnMenuItemClickListener {
            onOptionsItemSelected(it)
        }
        if (serializable != null) {
            user = serializable as User
            tv.text = user.toString();
        } else if (id != null) {
            loadUserData()
        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.title) {
            getString(R.string.delete) -> {
//                val intent = Intent(applicationContext, DashboardActivity::class.java)
//                startActivity(intent)
                id?.let {
                    ApiDao.deleteUser(it).enqueue(object : Callback<DeleteResult> {
                        override fun onFailure(call: Call<DeleteResult>, t: Throwable) {
                            Log.d("myApi", "Something went wrong");
                            Toast.makeText(
                                applicationContext,
                                "Something went wrong",
                                Toast.LENGTH_SHORT
                            ).show()
                        }

                        override fun onResponse(
                            call: Call<DeleteResult>,
                            response: Response<DeleteResult>
                        ) {
                            if (response.isSuccessful) {
                                Log.d("myApi", response.body().toString());
                                Toast.makeText(
                                    applicationContext,
                                    response.body()?.message,
                                    Toast.LENGTH_SHORT
                                ).show()
                                onBackPressed()
                            }
                        }
                    })

                }
            }
            getString(R.string.update) -> {
                ApiDao.updateUser(id!!,UserUpdate("d@gmail.com",user.name,user.school))
                    .enqueue(object:Callback<LoginResult>{
                        override fun onResponse(
                            call: Call<LoginResult>,
                            response: Response<LoginResult>
                        ) {
                            Toast.makeText(
                                applicationContext,
                                response.body()?.message,
                                Toast.LENGTH_SHORT
                            ).show()
                            loadUserData()
                        }

                        override fun onFailure(call: Call<LoginResult>, t: Throwable) {
                            Toast.makeText(
                                applicationContext,
                                "Something went wrong!",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    })
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun loadUserData() {
        ApiRepository(ApiDao)
            .getUser(id!!).enqueue(object : Callback<User> {
                override fun onResponse(
                    call: Call<User>,
                    response: Response<User>
                ) {
                    when {
                        response.isSuccessful -> {
                            user = response.body()!!
    //                            val error = response.body()?.error
                            Log.d("myApi", user.toString());
                            tv.text=""
                            topic.text = user.name
                            tv.append("User Name:")
                            tv.append(user.name)
                            tv.append("\n")
                            tv.append("Email:")
                            tv.append(user.email)
                            tv.append("\n")
                            tv.append("School:")
                            tv.append(user.school)
                            Toast.makeText(
                                applicationContext,
                                user.toString(),
                                Toast.LENGTH_SHORT
                            )
                                .show();
                        }
                        else -> {
                            Log.e("myApi", "response failed!! onResponse");
                            Log.e("myApi", "error code is ${response.code()}");
                            Toast.makeText(applicationContext, "Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    Log.e("myApi", "response failed!!");
                    Toast.makeText(
                        applicationContext,
                        "Some Error occur, please check your internet connection.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            })
    }


}