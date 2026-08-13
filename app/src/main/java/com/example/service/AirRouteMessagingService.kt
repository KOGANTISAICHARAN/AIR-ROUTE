package com.example.service

import android.util.Log
import com.example.utils.NotificationHelper
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class AirRouteMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)
        Log.d(TAG, "Refreshed FCM token: $token")
        // Store or register FCM token with backend server
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        Log.d(TAG, "From: ${remoteMessage.from}")

        // Check if message contains a notification payload
        val title = remoteMessage.notification?.title ?: remoteMessage.data["title"] ?: "AIRROUTE Alert 🌿"
        val body = remoteMessage.notification?.body ?: remoteMessage.data["body"] ?: "Air quality update available for your area."
        val screen = remoteMessage.data["targetScreen"] ?: "ACTIVITY"

        NotificationHelper.showNativeNotification(
            context = applicationContext,
            title = title,
            body = body,
            targetScreen = screen
        )
    }

    companion object {
        private const val TAG = "AirRouteFCMService"
    }
}
