package com.example.data

import androidx.compose.ui.graphics.Color
import com.example.utils.AqiCategory

enum class DataSourceType(val label: String, val badgeColor: Long) {
    OBSERVED("Observed • Live CPCB", 0xFF059669),
    PREDICTED("AIRROUTE Prediction", 0xFF0284C7),
    MODEL_ESTIMATED("Model Estimated", 0xFFD97706),
    DEMO("Evaluation Mode", 0xFF6B7280)
}

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
    val windDirection: String = "WSW",
    val precipitationMm: Double = 0.0,
    val weatherCondition: String = "Partly Cloudy",
    val weatherIcon: String = "⛅"
)

data class HourlyForecast(
    val timeLabel: String,
    val pm25: Int,
    val aqi: Int = pm25,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val isPredicted: Boolean = true,
    val timestampUtcMs: Long = System.currentTimeMillis(),
    val dataSourceType: DataSourceType = DataSourceType.PREDICTED,
    val pm10: Int = (pm25 * 1.3).toInt(),
    val no2: Int = 18,
    val tempC: Int = 28,
    val confidence: Int = 94
)

data class ScoreFactor(
    val title: String,
    val valueText: String,
    val impactText: String,
    val isPositive: Boolean
)

data class OutdoorScoreData(
    val score: Int = 82, // 0 to 100
    val headline: String = "Favorable Outdoor Conditions",
    val statusText: String = "Good time to go outside",
    val factors: List<ScoreFactor> = emptyList()
)

data class WhatIfOption(
    val timeLabel: String, // "GO NOW", "WAIT 2 HOURS", "WAIT 4 HOURS"
    val pm25: Int,
    val aqi: Int = pm25,
    val statusLabel: String,
    val recommendationState: RecommendationState
)

data class WhatIfWaitData(
    val goNow: WhatIfOption,
    val wait2h: WhatIfOption,
    val wait4h: WhatIfOption,
    val decisionAdvice: String
)

data class ModelMetaData(
    val modelName: String = "AIRROUTE Spatial-Temporal Predictor",
    val modelVersion: String = "v2.1.0-live",
    val lastTrainedDate: String = "August 2026",
    val validationMae: String = "3.8 µg/m³",
    val observedSource: String = "CPCB Stations / Open-Meteo Air Quality",
    val weatherSource: String = "Open-Meteo Weather API",
    val forecastEngine: String = "AIRROUTE ML Engine"
)

data class NotificationPreferences(
    val intervalHours: Int = 3, // 1, 2, 3, 6, 24
    val frequencyLabel: String = "Every 3 hours",
    val notifyOnImprovement: Boolean = true,
    val notifyOnRisingPollution: Boolean = true,
    val notifyOnBestWindow: Boolean = true,
    val notifyOnForecastChange: Boolean = true,
    val notifyOnDestinationAlert: Boolean = true,
    val isEnabled: Boolean = true
)

data class OutdoorActivity(
    val id: String,
    val title: String,
    val iconEmoji: String,
    val bestWindow: String,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val adviceText: String,
    val pm25Threshold: Int = 50
)

data class MapLocationNode(
    val id: String,
    val name: String,
    val area: String,
    val pm25: Int,
    val aqi: Int = pm25,
    val statusLabel: String,
    val recommendationState: RecommendationState,
    val latitude: Double,
    val longitude: Double,
    val xRatio: Float, // Position in 0..1 range on canvas
    val yRatio: Float,
    val quickAdvice: String,
    val coverageType: String = "CPCB Station",
    val dataSourceType: DataSourceType = DataSourceType.OBSERVED,
    val lastUpdated: String = "Updated 5m ago"
)

enum class RouteType(val label: String, val badgeColor: Long) {
    FASTEST("FASTEST", 0xFF0284C7),
    CLEANER_AIR("🌿 CLEANER AIR", 0xFF059669),
    BALANCED("BALANCED", 0xFFD97706)
}

data class RouteSegment(
    val name: String,
    val startRatio: Pair<Float, Float>,
    val endRatio: Pair<Float, Float>,
    val aqi: Int,
    val pm25: Int,
    val recommendationState: RecommendationState
)

data class RouteOption(
    val id: String,
    val type: RouteType,
    val title: String,
    val travelTimeMins: Int,
    val distanceKm: Double,
    val averageAqi: Int,
    val averagePm25: Int,
    val exposureLevel: String, // "LOW", "MODERATE", "HIGH"
    val recommendationText: String,
    val isRecommended: Boolean = false,
    val segments: List<RouteSegment> = emptyList()
)

data class PollutionHotspot(
    val id: String,
    val name: String,
    val aqi: Int,
    val pm25: Int,
    val severityLabel: String,
    val cause: String,
    val recommendationState: RecommendationState
)

data class CleanerPlace(
    val id: String,
    val name: String,
    val category: String, // "Park", "Walking Trail", "Botanical Garden"
    val distanceKm: Double,
    val aqi: Int,
    val pm25: Int,
    val statusLabel: String,
    val recommendationState: RecommendationState
)

data class PersonalExposureEstimate(
    val activityName: String,
    val durationMins: Int,
    val totalInhaledPm25Ug: Double,
    val exposureCategory: String, // "LOW EXPOSURE", "MODERATE EXPOSURE", "HIGH EXPOSURE"
    val colorHex: Long,
    val comparisonSubtext: String
)

data class AlertItem(
    val id: String,
    val iconEmoji: String,
    val category: String = "🌿 Better Window",
    val title: String,
    val description: String,
    val timeAgo: String,
    val timestampUtcMs: Long = System.currentTimeMillis(),
    val isRead: Boolean = false,
    val severity: RecommendationState = RecommendationState.CONSIDER_WAITING
)

data class UserProfile(
    val name: String = "Sai Charan",
    val email: String = "sai@airroute.app",
    val location: String = "Hyderabad, Telangana",
    val preferredActivities: List<String> = listOf("Running", "Walking"),
    val notificationPreferences: NotificationPreferences = NotificationPreferences(),
    val isLoggedIn: Boolean = true,
    val isDemoUser: Boolean = false
)

data class AirQualityStateData(
    val scenario: AirQualityScenario,
    val locationName: String = "Hyderabad, Telangana",
    val latitude: Double = 17.3850,
    val longitude: Double = 78.4867,
    val currentPm25: Int,
    val currentPm10: Int = 32,
    val currentNo2: Int = 18,
    val currentSo2: Int = 8,
    val currentO3: Int = 24,
    val currentCo: Double = 0.4,
    val currentAqi: Int = currentPm25,
    val aqiCategory: AqiCategory = AqiCategory.GOOD,
    val aqiStandardName: String = "Indian CPCB AQI",
    val statusLabel: String,
    val statusExplanation: String,
    val weather: WeatherData,
    val recommendationState: RecommendationState,
    val recommendationHeadline: String,
    val recommendationSubtext: String,
    val bestWindowTime: String,
    val bestWindowStatus: String,
    val bestWindowExplanation: String,
    val outdoorScore: OutdoorScoreData,
    val whatIfWait: WhatIfWaitData,
    val aiSummaryText: String,
    val aiConfidenceScore: Int,
    val aiTrendDirection: String,
    val hourlyForecast: List<HourlyForecast>,
    val forecast24h: List<HourlyForecast>,
    val mapNodes: List<MapLocationNode>,
    val routeOptions: List<RouteOption> = emptyList(),
    val hotspots: List<PollutionHotspot> = emptyList(),
    val cleanerPlaces: List<CleanerPlace> = emptyList(),
    val exposureEstimate: PersonalExposureEstimate? = null,
    val activitiesAdvice: List<OutdoorActivity>,
    val alerts: List<AlertItem>,
    val modelMetaData: ModelMetaData = ModelMetaData(),
    val lastUpdatedText: String = "Updated 5 mins ago",
    val dataSourceName: String = "CPCB Station / Open-Meteo Air Quality",
    val dataSourceType: DataSourceType = DataSourceType.OBSERVED,
    val isLiveRealData: Boolean = true,
    val highPollutionWarning: String? = null,
    val destinationWarning: String? = null,
    val plannedActivity: PlannedActivityData? = null,
    val activeSession: ActiveActivitySession? = null,
    val activityHistory: List<CompletedActivityItem> = emptyList()
)

data class PlannedActivityData(
    val activityId: String,
    val title: String,
    val emoji: String,
    val durationMins: Int = 30,
    val startOption: String = "NOW", // "NOW" or "SCHEDULED"
    val scheduledTimeLabel: String = "NOW",
    val locationName: String = "Hyderabad, Telangana",
    val destinationName: String? = null,
    val bestWindowTime: String = "4:00 PM – 5:00 PM",
    val currentAqi: Int = 42,
    val currentPm25: Int = 18,
    val weatherSummary: String = "28°C • Partly Cloudy",
    val estimatedExposure: String = "LOWER ESTIMATED EXPOSURE",
    val recommendationText: String = "Conditions are currently favorable for your planned activity.",
    val explanationText: String = "Air quality is expected to remain stable with moderate dispersion.",
    val isReminderSet: Boolean = false
)

data class ActiveActivitySession(
    val activityId: String,
    val title: String,
    val emoji: String,
    val targetDurationMins: Int = 30,
    val elapsedSeconds: Int = 0,
    val locationName: String = "Hyderabad, Telangana",
    val destinationName: String? = null,
    val startAqi: Int = 42,
    val currentAqi: Int = 42,
    val currentPm25: Int = 18,
    val weatherCondition: String = "28°C • Partly Cloudy",
    val isPaused: Boolean = false,
    val startTimestampMs: Long = System.currentTimeMillis(),
    val inAppWarning: String? = null
)

data class CompletedActivityItem(
    val id: String = java.util.UUID.randomUUID().toString(),
    val activityType: String,
    val emoji: String,
    val dateText: String,
    val durationMins: Int,
    val location: String,
    val destination: String? = null,
    val aqi: Int,
    val pm25: Int,
    val weather: String,
    val exposureCategory: String = "LOWER ESTIMATED EXPOSURE",
    val timestampMs: Long = System.currentTimeMillis()
)
