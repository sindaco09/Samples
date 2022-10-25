package com.indaco.samples.ui.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.google.assistant.appactions.widgets.AppActionsWidgetExtension
import com.indaco.samples.R

/**
 * Implementation of App Widget functionality.
 */
class WaitlistWidget : AppWidgetProvider() {
    override fun onUpdate(context: Context, appWidgetManager: AppWidgetManager, appWidgetIds: IntArray) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }

    fun updateAppWidget(context: Context, appWidgetManager: AppWidgetManager, appWidgetId: Int) {
        val widgetText = context.getString(R.string.appwidget_text)
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.waittime_widget)
        views.setTextViewText(R.id.tvName, widgetText)

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views)

        val appActionsWidgetExtension = AppActionsWidgetExtension.newBuilder(appWidgetManager)
            .setResponseSpeech("Waitlist Response")
            .build()

        appActionsWidgetExtension.updateWidget(appWidgetId)
    }
}

