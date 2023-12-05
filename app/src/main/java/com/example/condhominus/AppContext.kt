package com.example.condhominus

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class AppContext : Application() {

    override fun onCreate() {
        super.onCreate()
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }
}