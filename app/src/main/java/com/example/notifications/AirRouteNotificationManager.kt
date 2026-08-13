package com.example.notifications

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class AirRouteNotificationManager(private val context: Context) {

    companion object {
        const val CHANNEL_ID = "airroute_pollution_alerts"
        const val CHANNEL_NAME = "AIRROUTE Outdoor & Air Quality Alerts"
    }

    init {
        createNotificationChannel()
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance).apply {
                description = "Notifies when air quality improves, pollution rises, or ideal outdoor windows open."
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun sendNotification(title: String, message: String, notificationId: Int = System.currentTimeMillis().toInt()) {
        try {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle(title)
                .setContentText(message)
                .setStyle(NotificationCompat.BigTextStyle().bigText(message))
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)

            val notificationManager = NotificationManagerCompat.from(context)
            notificationManager.notify(notificationId, builder.build())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun triggerBestWindowAlert(activityName: String, windowTime: String) {
        sendNotification(
            title = "🌿 Optimal Outdoor Window Active",
            message = "Air quality is currently favorable for your $activityName. Best window: $windowTime."
        )
    }

    fun triggerPollutionRisingAlert(currentPm25: Int, forecastedPm25: Int) {
        sendNotification(
            title = "⚠️ High Pollution Alert",
            message = "PM2.5 is expected to increase from $currentPm25 to $forecastedPm25 µg/m³. Consider delaying outdoor plans or taking cleaner routes."
        )
    }

    fun triggerDestinationAlert(destination: String, originAqi: Int, destAqi: Int) {
        sendNotification(
            title = "⚠️ Destination Air Quality Alert",
            message = "Air quality at $destination (AQI $destAqi) is significantly worse than your origin (AQI $originAqi). Check cleaner routes in AIRROUTE."
        )
    }

    fun triggerForecastChangeAlert(timeSlot: String, oldPm25: Int, newPm25: Int) {
        sendNotification(
            title = "🔄 Forecast Updated",
            message = "AIRROUTE prediction for $timeSlot changed: PM2.5 updated from $oldPm25 to $newPm25 µg/m³."
        )
    }
}
