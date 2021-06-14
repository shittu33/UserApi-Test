package com.example.testapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.PopupMenu
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testapi.api.*
import com.example.testapi.databinding.ActivityDashboardBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {
    private lateinit var viewModel: DashboardViewModel
    private lateinit var adapter: UserAdapter
    private lateinit var binding: ActivityDashboardBinding

    private val rcv get() = findViewById<RecyclerView>(R.id.rcv)
    private val prg get() = findViewById<ProgressBar>(R.id.prg)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)
        viewModel()
        initViews()
//        setObservers()
    }

    override fun onResume() {
        super.onResume()
        setObservers()
    }

    private fun setObservers() {
        viewModel.getAllUsers().observe(this, Observer {
            doObserve(it)
        })
    }

    private fun doObserve(it: Resource<List<User>?>) {
        it.let { res ->
            when (res.status) {
                Status.SUCCESS -> {
                    rcv.visibility = View.VISIBLE
                    prg.visibility = View.GONE
                    res.data?.let { users ->
                        adapter.apply {
                            addUsers(users);
                            notifyDataSetChanged();
                        }
                    }
                    Log.e("myApi", res.data.toString());
                    Toast.makeText(applicationContext, "Success!", Toast.LENGTH_SHORT).show()
                }
                Status.LOADING -> {
                    rcv.visibility = View.GONE
                    prg.visibility = View.VISIBLE
                    Toast.makeText(applicationContext, "loading...", Toast.LENGTH_SHORT).show()
                }
                Status.ERROR -> {
                    rcv.visibility = View.VISIBLE
                    prg.visibility = View.GONE
                    Toast.makeText(applicationContext, it.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun viewModel() {
        viewModel =
            ViewModelProviders.of(this, DashModelFactory()).get(DashboardViewModel::class.java)
    }

    private fun initViews() {
        rcv.layoutManager = LinearLayoutManager(this)
        val onItemClick: (User) -> Unit = { user ->
            val intent = Intent(applicationContext, ProfileActivity::class.java)
            intent.putExtra("id", user.id);
            startActivity(intent)
        }
        val onOptionClick = { user: User, view: View ->
            val popupMenu = PopupMenu(applicationContext, view)
            popupMenu.inflate(R.menu.dash_menu)
            popupMenu.setOnMenuItemClickListener {
                when (it.title) {
                    getString(R.string.delete) -> {
                        viewModel.deleteUser(user.id).observe(this, Observer {
                            it.let { res ->
                                when (res.status) {
                                    Status.SUCCESS -> {
                                        setObservers()
                                        Log.e("myApi", res.data.toString());
                                        Toast.makeText(
                                            applicationContext,
                                            res.data?.message,
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }
                                    Status.LOADING -> {
                                        Toast.makeText(
                                            applicationContext,
                                            "loading...",
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                    Status.ERROR -> {
                                        Toast.makeText(
                                            applicationContext,
                                            it.message,
                                            Toast.LENGTH_SHORT
                                        )
                                            .show()
                                    }
                                }
                            }
                        })
                    }
                    getString(R.string.update) -> {
                        ApiDao.updateUser(user.id,UserUpdate("wwara@gmail.com", "Muslimah", "Jagun"))
                            .enqueue(object: Callback<LoginResult> {
                                override fun onResponse(
                                    call: Call<LoginResult>,
                                    response: Response<LoginResult>
                                ) {
                                    Toast.makeText(
                                        applicationContext,
                                        response.body()?.message,
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    setObservers()
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

                true
            }
            popupMenu.show()
        }
        val userAdapter =
            UserAdapter(arrayListOf(), onOptionClick = onOptionClick, onItemClick = onItemClick)
        adapter = userAdapter
        rcv.addItemDecoration(
            DividerItemDecoration(
                rcv.context,
                (rcv.layoutManager as LinearLayoutManager).orientation
            )
        )
        rcv.adapter = adapter;
    }

}
