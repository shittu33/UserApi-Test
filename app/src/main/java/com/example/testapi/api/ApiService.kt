package com.example.testapi.api

import android.util.Log
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

const val BASE_URL = "http://192.168.43.108/myApi/public/"
//const val BASE_URL = "http://192.168.42.138/myApi/public/"

interface ApiService {
    @POST("login")
    fun login(@Body() login: Login): Call<LoginResult>;

    @POST("createuser")
    fun register(@Body() body: Register): Call<LoginResult>;

    @GET("allusers")
    fun getAllUsers(): Call<UserListResult>;

    @GET("user/{id}")
    fun getUser(@Path("id") id: Int): Call<User>

    @PUT("updateuser/{id}")
    fun updateUser(@Path("id") id: Int,@Body() body: UserUpdate): Call<LoginResult>

    @PUT("updatepassword")
    fun updatePassword(@Path("id") id: Int): Call<LoginResult>

    @DELETE("deleteuser/{id}")
    fun deleteUser(@Path("id") id: Int): Call<DeleteResult>
}

object ApiDao {
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService = getRetrofit().create(ApiService::class.java);

    fun login(login: Login): Call<LoginResult> = apiService.login(login)

    fun register(body: Register): Call<LoginResult> = apiService.register(body)

    fun getAllUsers(): List<User> = apiService.getAllUsers().execute().body()!!.users

    fun getUser(id: Int): Call<User> = apiService.getUser(id)

    fun updateUser(id: Int,body: UserUpdate): Call<LoginResult> = apiService.updateUser(id,body)

    fun updatePassword(id: Int): Call<LoginResult> = apiService.updatePassword(id)

    fun deleteUser(id: Int): Call<DeleteResult> = apiService.deleteUser(id);

}