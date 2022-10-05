package com.indaco.samples

import android.app.Application
import com.indaco.samples.util.notifications.NotificationUtil
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