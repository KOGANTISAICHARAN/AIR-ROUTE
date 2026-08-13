package com.example.utils

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.TimeZone

object TimeUtils {

    fun getLocalTimeZone(): TimeZone {
        return TimeZone.getDefault() ?: TimeZone.getTimeZone("Asia/Kolkata")
    }

    fun getHyderabadTimeZone(): TimeZone = getLocalTimeZone()

    fun getCurrentHour(): Int {
        val calendar = Calendar.getInstance(getLocalTimeZone())
        return calendar.get(Calendar.HOUR_OF_DAY)
    }

    fun getCurrentHyderabadHour(): Int = getCurrentHour()

    fun getDynamicGreeting(userName: String, hourOfDay: Int = getCurrentHour()): String {
        val namePart = if (userName.isNotBlank()) ", $userName" else ""
        val greetingPrefix = when (hourOfDay) {
            in 5..11 -> "Good morning"
            in 12..16 -> "Good afternoon"
            in 17..20 -> "Good evening"
            else -> "Good night"
        }
        return "$greetingPrefix$namePart! 👋"
    }

    fun formatToLocalTime(timeMs: Long = System.currentTimeMillis()): String {
        val formatter = SimpleDateFormat("h:mm a", Locale.US)
        formatter.timeZone = getLocalTimeZone()
        return formatter.format(Date(timeMs))
    }

    fun formatToHyderabadTime(timeMs: Long = System.currentTimeMillis()): String = formatToLocalTime(timeMs)

    fun formatToHourLabel(timeMs: Long): String {
        val formatter = SimpleDateFormat("h a", Locale.US)
        formatter.timeZone = getLocalTimeZone()
        return formatter.format(Date(timeMs))
    }

    fun formatToHyderabadHourLabel(timeMs: Long): String = formatToHourLabel(timeMs)

    fun formatToFullDate(timeMs: Long = System.currentTimeMillis()): String {
        val formatter = SimpleDateFormat("EEE, d MMM yyyy • h:mm a", Locale.US)
        formatter.timeZone = getLocalTimeZone()
        return formatter.format(Date(timeMs))
    }

    fun formatToHyderabadFullDate(timeMs: Long = System.currentTimeMillis()): String = formatToFullDate(timeMs)

    fun getMinutesAgoText(timestampMs: Long): String {
        val diffMs = System.currentTimeMillis() - timestampMs
        val minutes = (diffMs / (1000 * 60)).toInt()
        return when {
            minutes <= 1 -> "Updated just now"
            minutes < 60 -> "Updated $minutes mins ago"
            else -> {
                val hours = minutes / 60
                "Updated $hours hrs ago"
            }
        }
    }
}
