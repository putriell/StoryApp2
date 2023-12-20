package com.example.storyapp2.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp2.data.pref.UserModel
import com.example.storyapp2.data.repository.Repository
import com.example.storyapp2.data.response.ListStoryItem
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository)  : ViewModel() {

    fun listStory(token: String): LiveData<PagingData<ListStoryItem>>{
        return repository.getStory().cachedIn(viewModelScope)
    }

    fun getUser(): LiveData<UserModel>{
        return repository.getSession()
    }

    fun logout(){
        viewModelScope.launch {
            repository.logout()
        }
    }

}

