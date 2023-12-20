package com.example.storyapp2.data.di


import android.content.Context
import com.example.storyapp2.data.pref.UserPreference
import com.example.storyapp2.data.pref.dataStore
import com.example.storyapp2.data.api.ApiConfig
import com.example.storyapp2.data.repository.Repository


object Injection {
    fun getRepository(context: Context): Repository {
        val preference = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return Repository.getInstance(preference, apiService)
    }
}