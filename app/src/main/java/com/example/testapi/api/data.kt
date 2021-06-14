package com.example.testapi.api

import java.io.Serializable


data class LoginResult(val error: Boolean, val message: String, val user: User);
data class UserListResult(val error: Boolean, val message: String,val count: Int, val users: List<User>);
data class DeleteResult(val error: Boolean, val message: String);
data class User(val id: Int,val email: String, val name: String, val school: String):Serializable
data class UserPassword(val email: String,val password: String,val newpassword:String)
data class Register(val email: String, val password: String, val name: String, val school: String)
data class UserUpdate(val email: String,  val name: String, val school: String)
data class Login(var email:String,var password:String)

data class Resource<out T>(val status:Status,val data:T?,val message: String?){
    companion object{
        fun <T> success(data:T):Resource<T> = Resource(status = Status.SUCCESS,data = data,message = null)
        fun <T> error(data:T,message: String):Resource<T> = Resource(status = Status.ERROR,data = data,message = message)
        fun <T> loading(data:T?):Resource<T> = Resource(status = Status.LOADING,data = data,message = null)

    }
}

enum class Status{
    SUCCESS,LOADING,ERROR
}