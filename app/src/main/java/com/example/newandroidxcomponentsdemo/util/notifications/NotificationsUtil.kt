package com.example.newandroidxcomponentsdemo.util.notifications

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.MainThread
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.core.app.TaskStackBuilder
import androidx.navigation.NavController
import androidx.navigation.NavDeepLink
import androidx.navigation.NavDeepLinkBuilder
import com.example.newandroidxcomponentsdemo.App
import com.example.newandroidxcomponentsdemo.R

object NotificationUtil {

    const val CHANNEL_ID = "app_channel"
    const val COFFEE_NOTIFICATION_ID = 3
    const val REQUEST_CODE = 3

    // Create the NotificationChannel, but only on API 26+ because
    // the NotificationChannel class is new and not in the support library
    fun createNotificationChannel(app: App) {
        val name = app.getString(R.string.channel_name)
        val descriptionText = app.getString(R.string.channel_description)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
            description = descriptionText
        }

        // Register the channel with the system
        val notificationManager: NotificationManager =
            app.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    @MainThread
    fun notifyApp(context: Context, navController: NavController) {
        val notification = Factory.buildCoffeeReadyNotification(context, navController)
        with(NotificationManagerCompat.from(context)) {
            notify(COFFEE_NOTIFICATION_ID, notification)
        }
    }

    object Factory {
        @MainThread
        fun buildCoffeeReadyNotification(context: Context, navController: NavController): Notification {

//            val deepLink = navController.createDeepLink().setDestination(R.id.coffeeCompleteFragment).createPendingIntent()

            val deepLink = NavDeepLinkBuilder(context)
                .setGraph(R.navigation.main_graph)
                .setDestination(R.id.coffeeFinalFragment)
                .createPendingIntent()


            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_coffee)
                .setContentTitle("Coffee Ready!")
                .setContentText("Your coffee is ready for pick-up!")
                .setContentIntent(deepLink)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)

            return builder.build()
        }

        private fun createPendingIntent(context: Context, intent: Intent): PendingIntent? =
            TaskStackBuilder.create(context).run {
                this.
                addNextIntentWithParentStack(intent)
                getPendingIntent(REQUEST_CODE, PendingIntent.FLAG_UPDATE_CURRENT)
            }
    }
}