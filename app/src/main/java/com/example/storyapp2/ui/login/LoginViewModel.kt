package com.example.storyapp2.ui.login

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.storyapp2.data.api.response.LoginResponse
import com.example.storyapp2.data.pref.UserModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp2.data.repository.Repository
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException

class LoginViewModel (private val repository: Repository) :ViewModel(){

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse


    fun login(email: String, password: String){
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.login(email, password)
                setAuth(
                    UserModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        email,
                        response.loginResult.token,
                        true
                    )


                )
                _isLoading.postValue(false)
                _loginResponse.postValue(response)
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                _isLoading.postValue(false)
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, LoginResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _loginResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    private fun setAuth(userModel: UserModel) {
        viewModelScope.launch {
            repository.setAuth(userModel)
        }
    }
}