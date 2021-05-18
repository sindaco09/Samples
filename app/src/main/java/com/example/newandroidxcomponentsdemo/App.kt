package com.example.newandroidxcomponentsdemo

import android.app.Application
import com.example.newandroidxcomponentsdemo.util.notifications.NotificationUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var Instance: App
    }

    override fun onCreate() {
        super.onCreate()
        Instance = this

        NotificationUtil.createNotificationChannel(this)
    }
}