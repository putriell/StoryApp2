package com.example.storyapp2.data.repository

import com.example.storyapp2.data.pref.UserPreference
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.paging.liveData
import androidx.paging.*
import com.example.storyapp2.data.api.ApiService
import com.example.storyapp2.data.pref.UserModel
import com.example.storyapp2.data.response.ListStoryItem
import com.example.storyapp2.ui.adapter.StoryPagingSource
import okhttp3.MultipartBody
import okhttp3.RequestBody


class Repository private constructor(private val preference: UserPreference, private val apiService: ApiService) {


    suspend fun register(name: String, email: String, password: String) =
        apiService.register(name, email, password)

    suspend fun login(email: String, password: String) =
        apiService.login(email, password)

    fun getStory(): LiveData<PagingData<ListStoryItem>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5,
                initialLoadSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService, preference)
            }
        ).liveData
    }


    suspend fun addNewStory(
        token: String,
        imageMultipart: MultipartBody.Part,
        description: RequestBody,
        lat: Double? = null,
        lon: Double? = null
    ) =
        apiService.addStory(token, imageMultipart, description, lat, lon)


    fun getSession() = preference.getSesion().asLiveData()

    suspend fun setAuth(user: UserModel) = preference.setAuth(user)

    suspend fun logout() = preference.logout()

    suspend fun getLocation(token: String) = apiService.getLocation(token)


    companion object {
        @Volatile
        private var INSTANCE: Repository? = null
        fun getInstance(
            preference: UserPreference,
            apiService: ApiService
        ): Repository =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: Repository(preference, apiService)
            }.also { INSTANCE = it }
    }
}