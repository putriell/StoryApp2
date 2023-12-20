package com.example.storyapp2.ui.Maps

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.storyapp2.data.repository.Repository
import com.example.storyapp2.data.response.StoryResponse
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException


class MapsViewModel(private val repository: Repository) : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading


    private val _mapsResponse = MutableLiveData<StoryResponse>()
    val mapsResponse: LiveData<StoryResponse> = _mapsResponse

    fun getLocation(token: String){
        _isLoading.postValue(true)
        viewModelScope.launch {
            try {
                val loc = repository.getLocation(token)
                _isLoading.postValue(false)
                _mapsResponse.postValue(loc)

            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = Gson().fromJson(jsonInString, StoryResponse::class.java)
                val errorMessage = errorBody.message
                _isLoading.postValue(false)
                Log.d(TAG, "onError: $errorMessage")
            }
        }
    }

    companion object {
        private const val TAG = "MainViewModel"
    }

}