package com.example.testapi.api

import retrofit2.Call

class ApiRepository(private val apiDao: ApiDao) {
    fun login(login: Login): Call<LoginResult> = apiDao.login(login)

    fun register(body: Register): Call<LoginResult> = apiDao.register(body)

     fun getAllUsers(): List<User> = apiDao.getAllUsers()

    fun getUser(id: Int): Call<User> = apiDao.getUser(id)

    fun updateUser(id: Int,body: UserUpdate): Call<LoginResult> = apiDao.updateUser(id,body)

    fun updatePassword(id: Int): Call<LoginResult> = apiDao.updatePassword(id)

    fun deleteUser(id: Int): DeleteResult = apiDao.deleteUser(id).execute().body()!!

}