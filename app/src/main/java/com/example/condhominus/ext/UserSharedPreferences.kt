package com.example.condhominus.ext

import android.app.Activity
import android.content.Context
import com.example.condhominus.model.Login
import com.google.gson.Gson

class UserSharedPreferences(activity: Activity) {

    private val sharedPreferences = activity.getSharedPreferences("preferences_session", Context.MODE_PRIVATE)
    private val userSaved = "user"

    fun saveUser(user: String) {
        sharedPreferences.edit().let{
            it.putString(userSaved, user)
            it.apply()
        }
    }

    fun getUserSaved(): Login? {
        return if (sharedPreferences.getString(userSaved, null) != null) {
            Gson().fromJson(sharedPreferences.getString(userSaved, null), Login::class.java)
        } else {
            null
        }
    }

    fun deleteUser() {
        sharedPreferences.edit().remove(userSaved).apply()
    }
}