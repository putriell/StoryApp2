package com.example.storyapp2.data.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "storyApp")

class UserPreference private constructor(private val context: DataStore<Preferences>){

    suspend fun setAuth(user: UserModel){
        context.edit { pref ->
            pref[USERID_KEY] = user.userId
            pref[NAME_KEY] = user.name
            pref[EMAIL_KEY] = user.email
            pref[TOKEN_KEY] = user.token
            pref[ISLOGIN_KEY] = true
        }

    }

    fun getSesion(): Flow<UserModel>{
        return context.data.map { pref ->
            UserModel(
                pref[USERID_KEY] ?: "",
                pref[NAME_KEY] ?: "",
                pref[EMAIL_KEY] ?: "",
                pref[TOKEN_KEY] ?: "",
                pref[ISLOGIN_KEY] ?: false
            )
        }
    }

     suspend fun logout() {
         context.edit { pref ->
             pref[USERID_KEY] = ""
             pref[NAME_KEY] = ""
             pref[EMAIL_KEY] = ""
             pref[TOKEN_KEY] = ""
             pref[ISLOGIN_KEY] = false
         }
    }

    companion object{
        @Volatile
        private var INSTANCE: UserPreference? = null


        private val USERID_KEY = stringPreferencesKey("userid")
        private val NAME_KEY = stringPreferencesKey("name")
        private val EMAIL_KEY = stringPreferencesKey("email")
        private val TOKEN_KEY = stringPreferencesKey("token")
        private val ISLOGIN_KEY = booleanPreferencesKey("isLogin")

        fun getInstance(dataStore: DataStore<Preferences>): UserPreference {
            return INSTANCE ?: synchronized(this) {
                val instance = UserPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}