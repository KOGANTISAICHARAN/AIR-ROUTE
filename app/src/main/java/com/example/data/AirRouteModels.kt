package com.example.data

import androidx.compose.ui.graphics.Color

enum class AirQualityScenario(val title: String, val badge: String) {
    GOOD("GOOD AIR", "🟢 GOOD"),
    MODERATE("MODERATE AIR", "🟡 MODERATE"),
    RISING_POLLUTION("RISING POLLUTION", "🟠 CAUTION"),
    HIGH_POLLUTION("HIGH POLLUTION", "🔴 UNHEALTHY")
}

enum class RecommendationState(
    val title: String,
    val badgeText: String,
    val hexColor: Long,
    val bgHexColor: Long
) {
    GOOD_TO_GO("GOOD TO GO", "🟢 GOOD TO GO", 0xFF059669, 0xFFD1FAE5),
    CONSIDER_WAITING("CONSIDER WAITING", "🟡 CONSIDER WAITING", 0xFFD97706, 0xFFFEF3C7),
    BETTER_INDOORS("BETTER INDOORS", "🔴 BETTER INDOORS", 0xFFDC2626, 0xFFFEE2E2)
}

data class WeatherData(
    val tempC: Int = 28,
    val humidityPercent: Int = 65,
    val windKmH: Int = 12,
    val windDirection: String = "WSW"
)

data class HourlyForecast(
    val timeLabel: String,
    val pm25: Int,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val isPredicted: Boolean = true
)

data class OutdoorActivity(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val bestWindow: String,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val adviceText: String
)

data class MapLocationNode(
    val id: String,
    val name: String,
    val area: String,
    val pm25: Int,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val xRatio: Float, // Position in 0..1 range
    val yRatio: Float,
    val quickAdvice: String
)

data class AlertItem(
    val id: String,
    val iconEmoji: String,
    val title: String,
    val description: String,
    val timeAgo: String,
    val isRead: Boolean = false,
    val severity: RecommendationState = RecommendationState.CONSIDER_WAITING
)

data class UserProfile(
    val name: String = "Sai Charan",
    val email: String = "sai@airroute.app",
    val location: String = "Hyderabad, Telangana",
    val preferredActivities: List<String> = listOf("Running", "Walking"),
    val notificationTime: String = "Morning",
    val isDemoUser: Boolean = true
)

data class AirQualityStateData(
    val scenario: AirQualityScenario,
    val locationName: String,
    val currentPm25: Int,
    val statusLabel: String,
    val statusExplanation: String,
    val weather: WeatherData,
    val recommendationState: RecommendationState,
    val recommendationHeadline: String,
    val recommendationSubtext: String,
    val bestWindowTime: String,
    val bestWindowStatus: String,
    val bestWindowExplanation: String,
    val aiSummaryText: String,
    val aiConfidenceScore: Int,
    val aiTrendDirection: String,
    val hourlyForecast: List<HourlyForecast>,
    val forecast24h: List<HourlyForecast>,
    val mapNodes: List<MapLocationNode>,
    val activitiesAdvice: List<OutdoorActivity>,
    val alerts: List<AlertItem>
)
