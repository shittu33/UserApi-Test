package com.example.testapi.api

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import java.lang.Exception
import java.lang.IllegalArgumentException

class DashboardViewModel(private val apiRepository: ApiRepository) : ViewModel() {

    fun getAllUsers() = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getAllUsers()))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error occurred"))
        }
    }

    fun getUser(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.getUser(id)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error occurred"))
        }
    }

    fun deleteUser(id: Int) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.deleteUser(id)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error occurred"))
        }
    }
    fun updateUser(id: Int,user:UserUpdate) = liveData(Dispatchers.IO) {
        emit(Resource.loading(data = null))
        try {
            emit(Resource.success(data = apiRepository.updateUser(id,user)))
        } catch (e: Exception) {
            emit(Resource.error(data = null, message = e.message ?: "Error occurred"))
        }
    }
}

class DashModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java))
            return DashboardViewModel(ApiRepository(ApiDao)) as T
        throw  IllegalArgumentException("Unknown class name")
    }

}