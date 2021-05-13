package com.example.newandroidxcomponentsdemo

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var Instance: App
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this
    }
}