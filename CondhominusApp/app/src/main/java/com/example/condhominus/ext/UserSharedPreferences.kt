package com.example.condhominus.ext

import android.app.Activity
import android.content.Context
import com.example.condhominus.R
import com.google.android.material.bottomnavigation.BottomNavigationView

class UserSharedPreferences(private val activity: Activity) {

    private val sharedPreferences = activity.getSharedPreferences("preferences_session", Context.MODE_PRIVATE)
    private val userDocument = "userDocument"

    fun saveUserDocument(value: String) {
        val bottomNavigation = activity.findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.apply {
            menu.findItem(R.id.navLogout).isVisible = true
            menu.findItem(R.id.navRegister).isVisible = true
        }
        val editor = sharedPreferences.edit()
        editor.putString(userDocument, value)
        editor.apply()
    }

    fun getUserDocument(): String? = sharedPreferences.getString(userDocument, null)

    fun deleteUserDocument() {
        val editor = sharedPreferences.edit()
        editor.remove(userDocument)
        editor.apply()
    }
}