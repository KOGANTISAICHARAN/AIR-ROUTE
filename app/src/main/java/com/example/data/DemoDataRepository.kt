package com.example.data

object DemoDataRepository {

    val AVAILABLE_LOCATIONS = listOf(
        "Hyderabad, Telangana",
        "Bengaluru, Karnataka",
        "New Delhi, DL",
        "Mumbai, Maharashtra",
        "San Francisco, CA"
    )

    fun getScenarioData(
        scenario: AirQualityScenario,
        selectedLocation: String = "Hyderabad, Telangana",
        selectedActivityId: String = "running"
    ): AirQualityStateData {
        return when (scenario) {
            AirQualityScenario.GOOD -> buildGoodAirScenario(selectedLocation, selectedActivityId)
            AirQualityScenario.MODERATE -> buildModerateAirScenario(selectedLocation, selectedActivityId)
            AirQualityScenario.RISING_POLLUTION -> buildRisingPollutionScenario(selectedLocation, selectedActivityId)
            AirQualityScenario.HIGH_POLLUTION -> buildHighPollutionScenario(selectedLocation, selectedActivityId)
        }
    }

    private fun buildGoodAirScenario(location: String, activityId: String): AirQualityStateData {
        val forecast6h = listOf(
            HourlyForecast("NOW", 42, "GOOD", RecommendationState.GOOD_TO_GO, isPredicted = false),
            HourlyForecast("4 PM", 48, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("5 PM", 57, "CAUTION", RecommendationState.CONSIDER_WAITING),
            HourlyForecast("6 PM", 64, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("7 PM", 68, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("8 PM", 61, "CAUTION", RecommendationState.CONSIDER_WAITING)
        )

        val forecast24h = listOf(
            HourlyForecast("12 PM", 38, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("3 PM", 42, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("6 PM", 64, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("9 PM", 55, "CAUTION", RecommendationState.CONSIDER_WAITING),
            HourlyForecast("12 AM", 35, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("3 AM", 28, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("6 AM", 32, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("9 AM", 40, "GOOD", RecommendationState.GOOD_TO_GO)
        )

        val activityName = when(activityId) {
            "walking" -> "walk"
            "cycling" -> "ride"
            "work" -> "outdoor work shift"
            "outing" -> "outdoor trip"
            else -> "run"
        }

        return AirQualityStateData(
            scenario = AirQualityScenario.GOOD,
            locationName = location,
            currentPm25 = 42,
            statusLabel = "GOOD",
            statusExplanation = "Air quality is looking good right now. Low particulate concentration detected.",
            weather = WeatherData(28, 65, 12, "WSW"),
            recommendationState = RecommendationState.GOOD_TO_GO,
            recommendationHeadline = "You're good to go!",
            recommendationSubtext = "Conditions look relatively clean right now. Perfect for your outdoor plans.",
            bestWindowTime = "3:00 PM – 4:00 PM",
            bestWindowStatus = "🟢 GOOD TO GO",
            bestWindowExplanation = "Air quality is currently favorable and is expected to worsen later this evening after 5 PM. If you're planning a $activityName, going now is your best option.",
            aiSummaryText = "Pollution is expected to rise after 5 PM due to evening traffic build-up and calm wind speeds. AIRROUTE recommends finishing your outdoor $activityName before 5:00 PM.",
            aiConfidenceScore = 94,
            aiTrendDirection = "Stable then Rising (+26 µg/m³ by 7 PM)",
            hourlyForecast = forecast6h,
            forecast24h = forecast24h,
            mapNodes = listOf(
                MapLocationNode("1", "Gachibowli Park", "West Hyderabad", 38, "GOOD", RecommendationState.GOOD_TO_GO, 0.25f, 0.35f, "Great air quality for jogging."),
                MapLocationNode("2", "Hitech City Metro", "Tech Hub", 42, "GOOD", RecommendationState.GOOD_TO_GO, 0.45f, 0.28f, "Clean conditions across major walkways."),
                MapLocationNode("3", "KBR National Park", "Jubilee Hills", 35, "GOOD", RecommendationState.GOOD_TO_GO, 0.65f, 0.50f, "Very low PM2.5 among tree cover."),
                MapLocationNode("4", "Panjagutta Junction", "Central Axis", 55, "CAUTION", RecommendationState.CONSIDER_WAITING, 0.52f, 0.72f, "Slight traffic emissions build-up."),
                MapLocationNode("5", "Charminar Heritage Zone", "Old City", 68, "HIGH", RecommendationState.BETTER_INDOORS, 0.78f, 0.82f, "High vehicular density in narrow streets.")
            ),
            activitiesAdvice = listOf(
                OutdoorActivity("running", "Running", "🏃", "Best before 4 PM", "GOOD", RecommendationState.GOOD_TO_GO, "Favorable air quality now. Complete runs before 5:00 PM."),
                OutdoorActivity("walking", "Walking", "🚶", "Good time for a walk", "GOOD", RecommendationState.GOOD_TO_GO, "Ideal conditions for light or brisk walks."),
                OutdoorActivity("cycling", "Cycling", "🚴", "Consider going earlier", "MODERATE", RecommendationState.CONSIDER_WAITING, "Hydrate well and stay away from main arterial roads."),
                OutdoorActivity("work", "Outdoor Work", "🌳", "Favorable shift window", "GOOD", RecommendationState.GOOD_TO_GO, "Clean air window available for the next 2 hours."),
                OutdoorActivity("outing", "General Outdoor", "☀️", "Great for outdoor seating", "GOOD", RecommendationState.GOOD_TO_GO, "Enjoy outdoor dining and park visits now.")
            ),
            alerts = listOf(
                AlertItem("a1", "⚠️", "Pollution Rising Later", "PM2.5 is expected to increase this evening after 5:00 PM.", "10 mins ago"),
                AlertItem("a2", "🌿", "Optimal Window Active", "3:00 PM – 4:00 PM is currently your prime outdoor window.", "25 mins ago"),
                AlertItem("a3", "🔔", "Short-term Forecast Sync", "New AI short-term trend prediction loaded successfully.", "1 hour ago")
            )
        )
    }

    private fun buildModerateAirScenario(location: String, activityId: String): AirQualityStateData {
        val forecast6h = listOf(
            HourlyForecast("NOW", 78, "MODERATE", RecommendationState.CONSIDER_WAITING, isPredicted = false),
            HourlyForecast("4 PM", 72, "MODERATE", RecommendationState.CONSIDER_WAITING),
            HourlyForecast("5 PM", 58, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("6 PM", 52, "GOOD", RecommendationState.GOOD_TO_GO),
            HourlyForecast("7 PM", 60, "MODERATE", RecommendationState.CONSIDER_WAITING),
            HourlyForecast("8 PM", 75, "MODERATE", RecommendationState.CONSIDER_WAITING)
        )

        return AirQualityStateData(
            scenario = AirQualityScenario.MODERATE,
            locationName = location,
            currentPm25 = 78,
            statusLabel = "MODERATE",
            statusExplanation = "Moderate particulate matter present. Acceptable for most people, but sensitive individuals should monitor.",
            weather = WeatherData(30, 58, 16, "SW"),
            recommendationState = RecommendationState.CONSIDER_WAITING,
            recommendationHeadline = "Maybe wait a little",
            recommendationSubtext = "Air quality is expected to improve significantly around 5:00 PM as wind speeds increase.",
            bestWindowTime = "5:00 PM – 6:30 PM",
            bestWindowStatus = "🟢 GOOD TO GO LATER",
            bestWindowExplanation = "Current particulate dispersion is moderate right now. A fresh air pocket is forecasted between 5:00 PM and 6:30 PM.",
            aiSummaryText = "Winds are picking up from the Southwest, clearing airborne PM2.5. Postponing high-exertion outdoor activities until 5 PM is recommended.",
            aiConfidenceScore = 91,
            aiTrendDirection = "Clearing Trend (-26 µg/m³ by 6 PM)",
            hourlyForecast = forecast6h,
            forecast24h = forecast6h,
            mapNodes = listOf(
                MapLocationNode("1", "Gachibowli Park", "West Hyderabad", 65, "MODERATE", RecommendationState.CONSIDER_WAITING, 0.25f, 0.35f, "Fair air quality, improving soon."),
                MapLocationNode("2", "Hitech City Metro", "Tech Hub", 78, "MODERATE", RecommendationState.CONSIDER_WAITING, 0.45f, 0.28f, "Moderate dust particulates near flyover."),
                MapLocationNode("3", "KBR National Park", "Jubilee Hills", 58, "GOOD", RecommendationState.GOOD_TO_GO, 0.65f, 0.50f, "Protected tree area has lower PM2.5."),
                MapLocationNode("4", "Panjagutta Junction", "Central Axis", 88, "CAUTION", RecommendationState.CONSIDER_WAITING, 0.52f, 0.72f, "High traffic density."),
                MapLocationNode("5", "Charminar Heritage Zone", "Old City", 95, "CAUTION", RecommendationState.CONSIDER_WAITING, 0.78f, 0.82f, "Elevated dust accumulation.")
            ),
            activitiesAdvice = listOf(
                OutdoorActivity("running", "Running", "🏃", "Best around 5:00 PM", "MODERATE", RecommendationState.CONSIDER_WAITING, "Consider light pace or waiting until 5 PM for cleaner air."),
                OutdoorActivity("walking", "Walking", "🚶", "Suitable for casual walk", "MODERATE", RecommendationState.CONSIDER_WAITING, "Okay for low exertion. Avoid heavy cardio."),
                OutdoorActivity("cycling", "Cycling", "🚴", "Wait for wind clearing", "CAUTION", RecommendationState.CONSIDER_WAITING, "Wear an N95 anti-dust bandana if riding on main roads."),
                OutdoorActivity("work", "Outdoor Work", "🌳", "Schedule breaks", "MODERATE", RecommendationState.CONSIDER_WAITING, "Take hydration breaks away from roadside construction."),
                OutdoorActivity("outing", "General Outdoor", "☀️", "Good in shaded parks", "GOOD", RecommendationState.GOOD_TO_GO, "Parks and tree-covered areas are pleasant.")
            ),
            alerts = listOf(
                AlertItem("a1", "🌿", "Clearing Trend Expected", "Wind shifts will clear PM2.5 around 5:00 PM today.", "5 mins ago"),
                AlertItem("a2", "⚠️", "Moderate Dust Notice", "Roadside particulate counts are slightly elevated.", "40 mins ago")
            )
        )
    }

    private fun buildRisingPollutionScenario(location: String, activityId: String): AirQualityStateData {
        val forecast6h = listOf(
            HourlyForecast("NOW", 115, "CAUTION", RecommendationState.CONSIDER_WAITING, isPredicted = false),
            HourlyForecast("4 PM", 132, "CAUTION", RecommendationState.CONSIDER_WAITING),
            HourlyForecast("5 PM", 155, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("6 PM", 178, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("7 PM", 185, "HIGH", RecommendationState.BETTER_INDOORS),
            HourlyForecast("8 PM", 160, "HIGH", RecommendationState.BETTER_INDOORS)
        )

        return AirQualityStateData(
            scenario = AirQualityScenario.RISING_POLLUTION,
            locationName = location,
            currentPm25 = 115,
            statusLabel = "CAUTION",
            statusExplanation = "Unhealthy for sensitive groups. Fine particulate concentration is rising steadily across the area.",
            weather = WeatherData(32, 45, 6, "CALM"),
            recommendationState = RecommendationState.CONSIDER_WAITING,
            recommendationHeadline = "Pollution is rising quickly",
            recommendationSubtext = "Stagnant wind conditions are trapping pollutants. Outdoor exertion is discouraged.",
            bestWindowTime = "NOW – 3:30 PM (Closing)",
            bestWindowStatus = "🟡 CLOSING FAST",
            bestWindowExplanation = "Pollution levels will cross unsafe thresholds past 5:00 PM. Wrap up essential outdoor tasks immediately or move indoors.",
            aiSummaryText = "Calm wind speed (6 km/h) combined with temperature inversion is trapping industrial and vehicular emissions. Levels will peak at 185 µg/m³ around 7 PM.",
            aiConfidenceScore = 89,
            aiTrendDirection = "Sharply Rising (+70 µg/m³ by 7 PM)",
            hourlyForecast = forecast6h,
            forecast24h = forecast6h,
            mapNodes = listOf(
                MapLocationNode("1", "Gachibowli Park", "West Hyderabad", 102, "CAUTION", RecommendationState.CONSIDER_WAITING, 0.25f, 0.35f, "Rising smog cloud nearby."),
                MapLocationNode("2", "Hitech City Metro", "Tech Hub", 125, "CAUTION", RecommendationState.CONSIDER_WAITING, 0.45f, 0.28f, "Heavy exhaust haze visible."),
                MapLocationNode("3", "KBR National Park", "Jubilee Hills", 92, "MODERATE", RecommendationState.CONSIDER_WAITING, 0.65f, 0.50f, "Slightly buffered by trees."),
                MapLocationNode("4", "Panjagutta Junction", "Central Axis", 158, "HIGH", RecommendationState.BETTER_INDOORS, 0.52f, 0.72f, "High traffic smog accumulation."),
                MapLocationNode("5", "Charminar Heritage Zone", "Old City", 172, "HIGH", RecommendationState.BETTER_INDOORS, 0.78f, 0.82f, "Poor air circulation.")
            ),
            activitiesAdvice = listOf(
                OutdoorActivity("running", "Running", "🏃", "Not recommended today", "CAUTION", RecommendationState.CONSIDER_WAITING, "High heart rate will inhale concentrated PM2.5. Consider indoor treadmill."),
                OutdoorActivity("walking", "Walking", "🚶", "Keep walks short", "CAUTION", RecommendationState.CONSIDER_WAITING, "Limit walking time and wear a protective mask if outside."),
                OutdoorActivity("cycling", "Cycling", "🚴", "Avoid outdoor riding", "HIGH", RecommendationState.BETTER_INDOORS, "Cardio exertion in high smog is unhealthy."),
                OutdoorActivity("work", "Outdoor Work", "🌳", "Wear protective mask", "CAUTION", RecommendationState.CONSIDER_WAITING, "Use N95 filter masks for outdoor physical labor."),
                OutdoorActivity("outing", "General Outdoor", "☀️", "Prefer air-conditioned spaces", "CAUTION", RecommendationState.CONSIDER_WAITING, "Move outdoor gatherings indoors.")
            ),
            alerts = listOf(
                AlertItem("a1", "⚠️", "Pollution Alert: Sharp Inversion", "Low winds trapping particulate emissions across the city.", "2 mins ago", severity = RecommendationState.BETTER_INDOORS),
                AlertItem("a2", "🔔", "Outdoor Window Closing", "Air quality deteriorating faster than morning baseline.", "15 mins ago")
            )
        )
    }

    private fun buildHighPollutionScenario(location: String, activityId: String): AirQualityStateData {
        val forecast6h = listOf(
            HourlyForecast("NOW", 168, "UNHEALTHY", RecommendationState.BETTER_INDOORS, isPredicted = false),
            HourlyForecast("4 PM", 182, "UNHEALTHY", RecommendationState.BETTER_INDOORS),
            HourlyForecast("5 PM", 195, "UNHEALTHY", RecommendationState.BETTER_INDOORS),
            HourlyForecast("6 PM", 210, "VERY UNHEALTHY", RecommendationState.BETTER_INDOORS),
            HourlyForecast("7 PM", 225, "VERY UNHEALTHY", RecommendationState.BETTER_INDOORS),
            HourlyForecast("8 PM", 190, "UNHEALTHY", RecommendationState.BETTER_INDOORS)
        )

        return AirQualityStateData(
            scenario = AirQualityScenario.HIGH_POLLUTION,
            locationName = location,
            currentPm25 = 168,
            statusLabel = "UNHEALTHY",
            statusExplanation = "High pollution event. Everyone may begin to experience health effects; members of sensitive groups may experience more serious effects.",
            weather = WeatherData(27, 82, 4, "NNE"),
            recommendationState = RecommendationState.BETTER_INDOORS,
            recommendationHeadline = "Better to stay indoors for now",
            recommendationSubtext = "Pollution is currently high and is expected to remain elevated throughout the evening.",
            bestWindowTime = "Indoor Activity Preferred",
            bestWindowStatus = "🔴 INDOORS RECOMMENDED",
            bestWindowExplanation = "Outdoor air quality is significantly degraded. AIRROUTE strongly advises shifting workouts, walks, and leisure activities to indoor spaces.",
            aiSummaryText = "Severe particulate smog event detected. High humidity (82%) and near-zero surface wind are locking PM2.5 close to the ground. Re-evaluate tomorrow morning.",
            aiConfidenceScore = 96,
            aiTrendDirection = "Hazardous High Peak (225 µg/m³ at 7 PM)",
            hourlyForecast = forecast6h,
            forecast24h = forecast6h,
            mapNodes = listOf(
                MapLocationNode("1", "Gachibowli Park", "West Hyderabad", 152, "UNHEALTHY", RecommendationState.BETTER_INDOORS, 0.25f, 0.35f, "Thick smog layer across park area."),
                MapLocationNode("2", "Hitech City Metro", "Tech Hub", 175, "UNHEALTHY", RecommendationState.BETTER_INDOORS, 0.45f, 0.28f, "Severe dust and exhaust haze."),
                MapLocationNode("3", "KBR National Park", "Jubilee Hills", 140, "UNHEALTHY", RecommendationState.BETTER_INDOORS, 0.65f, 0.50f, "High PM2.5 readings even in woods."),
                MapLocationNode("4", "Panjagutta Junction", "Central Axis", 210, "VERY HIGH", RecommendationState.BETTER_INDOORS, 0.52f, 0.72f, "Hazardous traffic emissions trap."),
                MapLocationNode("5", "Charminar Heritage Zone", "Old City", 225, "VERY HIGH", RecommendationState.BETTER_INDOORS, 0.78f, 0.82f, "Heavy particulate concentration.")
            ),
            activitiesAdvice = listOf(
                OutdoorActivity("running", "Running", "🏃", "Indoor treadmill only", "HIGH", RecommendationState.BETTER_INDOORS, "Avoid outdoor cardio. Run indoors in air-filtered environments."),
                OutdoorActivity("walking", "Walking", "🚶", "Indoor mall / treadmill", "HIGH", RecommendationState.BETTER_INDOORS, "Postpone leisure walks outdoors."),
                OutdoorActivity("cycling", "Cycling", "🚴", "Indoor stationary bike", "HIGH", RecommendationState.BETTER_INDOORS, "Do not ride outdoors today."),
                OutdoorActivity("work", "Outdoor Work", "🌳", "Mandatory N95 mask", "HIGH", RecommendationState.BETTER_INDOORS, "Require full respirators for essential outdoor crews."),
                OutdoorActivity("outing", "General Outdoor", "☀️", "Stay indoors with HEPA filter", "HIGH", RecommendationState.BETTER_INDOORS, "Keep windows closed and run indoor air purifiers.")
            ),
            alerts = listOf(
                AlertItem("a1", "🚨", "High Pollution Spike Warning", "PM2.5 exceeded 160 µg/m³ threshold. Stay indoors.", "Just now", severity = RecommendationState.BETTER_INDOORS),
                AlertItem("a2", "⚠️", "Air Purifier Advisory", "Indoor air filtration recommended for sensitive individuals.", "30 mins ago", severity = RecommendationState.BETTER_INDOORS)
            )
        )
    }
}
