package com.example.data

import android.content.Context
import android.content.SharedPreferences

class UserPreferencesRepository(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("airroute_user_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_IS_LOGGED_IN = "is_logged_in"
        private const val KEY_USER_NAME = "user_name"
        private const val KEY_USER_EMAIL = "user_email"
        private const val KEY_LOCATION = "location"
        private const val KEY_NOTIF_INTERVAL = "notif_interval"
        private const val KEY_NOTIF_IMPROVEMENT = "notif_improvement"
        private const val KEY_NOTIF_RISING = "notif_rising"
        private const val KEY_NOTIF_WINDOW = "notif_window"
        private const val KEY_NOTIF_CHANGE = "notif_change"
        private const val KEY_ACTIVITIES = "preferred_activities"
    }

    fun isLoggedIn(): Boolean = prefs.getBoolean(KEY_IS_LOGGED_IN, true)

    fun getUserProfile(): UserProfile {
        val name = prefs.getString(KEY_USER_NAME, "Sai Charan") ?: "Sai Charan"
        val email = prefs.getString(KEY_USER_EMAIL, "sai@airroute.app") ?: "sai@airroute.app"
        val location = prefs.getString(KEY_LOCATION, "Hyderabad, Telangana") ?: "Hyderabad, Telangana"
        val interval = prefs.getInt(KEY_NOTIF_INTERVAL, 3)
        val notifImprovement = prefs.getBoolean(KEY_NOTIF_IMPROVEMENT, true)
        val notifRising = prefs.getBoolean(KEY_NOTIF_RISING, true)
        val notifWindow = prefs.getBoolean(KEY_NOTIF_WINDOW, true)
        val notifChange = prefs.getBoolean(KEY_NOTIF_CHANGE, true)
        val activitiesStr = prefs.getString(KEY_ACTIVITIES, "Running,Walking") ?: "Running,Walking"

        val frequencyLabel = when (interval) {
            1 -> "Every 1 hour"
            2 -> "Every 2 hours"
            3 -> "Every 3 hours"
            6 -> "Every 6 hours"
            24 -> "Once every morning"
            else -> "Every 3 hours"
        }

        val notifPrefs = NotificationPreferences(
            intervalHours = interval,
            frequencyLabel = frequencyLabel,
            notifyOnImprovement = notifImprovement,
            notifyOnRisingPollution = notifRising,
            notifyOnBestWindow = notifWindow,
            notifyOnForecastChange = notifChange
        )

        return UserProfile(
            name = name,
            email = email,
            location = location,
            preferredActivities = activitiesStr.split(",").filter { it.isNotBlank() },
            notificationPreferences = notifPrefs,
            isLoggedIn = isLoggedIn(),
            isDemoUser = false
        )
    }

    fun saveUserSession(name: String, email: String) {
        prefs.edit()
            .putBoolean(KEY_IS_LOGGED_IN, true)
            .putString(KEY_USER_NAME, name)
            .putString(KEY_USER_EMAIL, email)
            .apply()
    }

    fun saveLocation(location: String) {
        prefs.edit().putString(KEY_LOCATION, location).apply()
    }

    fun saveNotificationPreferences(notifPrefs: NotificationPreferences) {
        prefs.edit()
            .putInt(KEY_NOTIF_INTERVAL, notifPrefs.intervalHours)
            .putBoolean(KEY_NOTIF_IMPROVEMENT, notifPrefs.notifyOnImprovement)
            .putBoolean(KEY_NOTIF_RISING, notifPrefs.notifyOnRisingPollution)
            .putBoolean(KEY_NOTIF_WINDOW, notifPrefs.notifyOnBestWindow)
            .putBoolean(KEY_NOTIF_CHANGE, notifPrefs.notifyOnForecastChange)
            .apply()
    }

    fun savePreferredActivities(activities: List<String>) {
        prefs.edit().putString(KEY_ACTIVITIES, activities.joinToString(",")).apply()
    }

    fun clearSession() {
        prefs.edit().putBoolean(KEY_IS_LOGGED_IN, false).apply()
    }
}
