package com.example.printersample

import android.app.Application
import android.content.SharedPreferences

class App : Application(){
    override fun onCreate() {
        super.onCreate()
        SharedPreferenceManager.init(this)
    }
}