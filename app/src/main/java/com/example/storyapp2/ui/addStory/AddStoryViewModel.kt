package com.example.storyapp2.ui.addStory

import android.content.ContentValues.TAG
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp2.data.pref.UserModel
import com.example.storyapp2.data.repository.Repository
import com.example.storyapp2.data.response.AddStoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.HttpException


class AddStoryViewModel (private val repository: Repository) : ViewModel() {

    private val _errorResponse = MutableLiveData<AddStoryResponse>()
    val errorResponse: LiveData<AddStoryResponse> = _errorResponse

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    fun addNewStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val apiResponse = repository.addNewStory(token, imageMultipart, description, lat, lon)
                _errorResponse.postValue(apiResponse)
                Log.d(TAG, "onSuccess: ${apiResponse.message}")
                _isLoading.value = false
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, AddStoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                _errorResponse.postValue(errorBody)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    fun getUser(): LiveData<UserModel> {
        return repository.getSession()
    }


}