package com.example.storyapp2.data.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.storyapp2.data.di.Injection
import com.example.storyapp2.data.repository.Repository
import com.example.storyapp2.ui.Maps.MapsViewModel
import com.example.storyapp2.ui.addStory.AddStoryViewModel
import com.example.storyapp2.ui.login.LoginViewModel
import com.example.storyapp2.ui.main.MainViewModel
import com.example.storyapp2.ui.register.RegisterViewModel

class ViewModelFactory(private val repository: Repository) :
ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(RegisterViewModel::class.java) -> {
                RegisterViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(LoginViewModel::class.java) -> {
                LoginViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(MainViewModel::class.java) -> {
                MainViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(AddStoryViewModel::class.java) -> {
                AddStoryViewModel(this.repository) as T
            }
            modelClass.isAssignableFrom(MapsViewModel::class.java) -> {
                MapsViewModel(this.repository) as T
            }
            else -> {
                throw IllegalArgumentException("ViewModel Not Found: ${modelClass.name}")
            }
        }
    }


    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        @JvmStatic
        fun getInstance(context: Context): ViewModelFactory {
            if (instance == null) {
                synchronized(ViewModelFactory::class.java) {
                    instance= ViewModelFactory(Injection.getRepository(context))
                }
            }
            return instance as ViewModelFactory
        }
    }
}