package com.example.utils

import androidx.compose.ui.graphics.Color
import com.example.data.RecommendationState
import kotlin.math.roundToInt

enum class AqiCategory(
    val title: String,
    val badgeText: String,
    val hexColor: Long,
    val bgHexColor: Long,
    val advice: String
) {
    GOOD(
        title = "Good",
        badgeText = "🟢 GOOD",
        hexColor = 0xFF059669,
        bgHexColor = 0xFFD1FAE5,
        advice = "Air quality is favorable. Ideal for all outdoor activities."
    ),
    SATISFACTORY(
        title = "Satisfactory",
        badgeText = "🟢 SATISFACTORY",
        hexColor = 0xFF10B981,
        bgHexColor = 0xFFE6F4EA,
        advice = "Minor breathing discomfort to sensitive people."
    ),
    MODERATE(
        title = "Moderate",
        badgeText = "🟡 MODERATE",
        hexColor = 0xFFD97706,
        bgHexColor = 0xFFFEF3C7,
        advice = "Breathing discomfort to people with lung disease, asthma, and heart diseases."
    ),
    POOR(
        title = "Poor",
        badgeText = "🟠 POOR",
        hexColor = 0xFFEA580C,
        bgHexColor = 0xFFFFEDD5,
        advice = "Breathing discomfort to most people on prolonged exposure."
    ),
    VERY_POOR(
        title = "Very Poor",
        badgeText = "🔴 VERY POOR",
        hexColor = 0xFFDC2626,
        bgHexColor = 0xFFFEE2E2,
        advice = "Respiratory illness on prolonged exposure. Avoid strenuous outdoor activities."
    ),
    SEVERE(
        title = "Severe / Hazardous",
        badgeText = "🟣 SEVERE",
        hexColor = 0xFF7C3AED,
        bgHexColor = 0xFFF3E8FF,
        advice = "Affects healthy people and seriously impacts those with existing diseases."
    )
}

data class AqiResult(
    val aqiValue: Int,
    val category: AqiCategory,
    val recommendationState: RecommendationState,
    val standardName: String = "Indian CPCB AQI"
)

object AqiUtils {

    /**
     * Calculates Indian CPCB Air Quality Index from PM2.5 (µg/m³) and PM10 (µg/m³).
     * Reference: Central Pollution Control Board (CPCB) India National Air Quality Index methodology.
     */
    fun calculateIndianAqi(pm25: Double, pm10: Double = pm25 * 1.3): AqiResult {
        val pm25Aqi = calculateSubIndex(
            c = pm25,
            breakpoints = doubleArrayOf(0.0, 30.0, 60.0, 90.0, 120.0, 250.0, 380.0, 500.0),
            aqiBreakpoints = doubleArrayOf(0.0, 50.0, 100.0, 200.0, 300.0, 400.0, 500.0, 500.0)
        )

        val pm10Aqi = calculateSubIndex(
            c = pm10,
            breakpoints = doubleArrayOf(0.0, 50.0, 100.0, 250.0, 350.0, 430.0, 500.0, 600.0),
            aqiBreakpoints = doubleArrayOf(0.0, 50.0, 100.0, 200.0, 300.0, 400.0, 500.0, 500.0)
        )

        val finalAqi = maxOf(pm25Aqi, pm10Aqi).roundToInt().coerceIn(1, 500)

        val category = when {
            finalAqi <= 50 -> AqiCategory.GOOD
            finalAqi <= 100 -> AqiCategory.SATISFACTORY
            finalAqi <= 200 -> AqiCategory.MODERATE
            finalAqi <= 300 -> AqiCategory.POOR
            finalAqi <= 400 -> AqiCategory.VERY_POOR
            else -> AqiCategory.SEVERE
        }

        val recState = when (category) {
            AqiCategory.GOOD, AqiCategory.SATISFACTORY -> RecommendationState.GOOD_TO_GO
            AqiCategory.MODERATE, AqiCategory.POOR -> RecommendationState.CONSIDER_WAITING
            AqiCategory.VERY_POOR, AqiCategory.SEVERE -> RecommendationState.BETTER_INDOORS
        }

        return AqiResult(
            aqiValue = finalAqi,
            category = category,
            recommendationState = recState,
            standardName = "Indian CPCB AQI"
        )
    }

    private fun calculateSubIndex(c: Double, breakpoints: DoubleArray, aqiBreakpoints: DoubleArray): Double {
        if (c <= 0.0) return 0.0
        for (i in 0 until breakpoints.size - 1) {
            val cLow = breakpoints[i]
            val cHigh = breakpoints[i + 1]
            if (c in cLow..cHigh) {
                val iLow = aqiBreakpoints[i]
                val iHigh = aqiBreakpoints[i + 1]
                return iLow + ((iHigh - iLow) / (cHigh - cLow)) * (c - cLow)
            }
        }
        return 500.0
    }

    fun getExplanationText(): String {
        return "PM2.5 measures microscopic airborne particulate matter concentration in micrograms per cubic meter (µg/m³). AQI (Air Quality Index) is a calculated score from 0 to 500 that translates pollutant concentrations into an easily understood health advisory scale."
    }
}
