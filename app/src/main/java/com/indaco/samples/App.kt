package com.indaco.samples

import android.app.Application
import com.indaco.samples.util.notifications.NotificationUtil
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class App: Application() {
    companion object {
        lateinit var Instance: com.indaco.samples.App
    }

    override fun onCreate() {
        super.onCreate()
        com.indaco.samples.App.Companion.Instance = this

        NotificationUtil.createNotificationChannel(this)
    }
}