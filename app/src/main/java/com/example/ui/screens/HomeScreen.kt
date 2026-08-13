package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AirQualityStateData
import com.example.ui.components.AirQualityCard
import com.example.ui.components.AirRouteAICard
import com.example.ui.components.BestWindowCard
import com.example.ui.components.GoingOutsideSection
import com.example.ui.components.HotspotsAndCleanerPlacesCard
import com.example.ui.components.OutdoorScoreCard
import com.example.ui.components.PersonalExposureCard
import com.example.ui.components.TodayOutlookTimeline
import com.example.ui.components.WhatIfWaitCard
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextSecondary
import com.example.utils.TimeUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    data: AirQualityStateData,
    selectedActivityId: String,
    unreadAlertCount: Int,
    onActivitySelected: (String) -> Unit,
    onWhyScoreClicked: () -> Unit,
    onWhyThisTimeClicked: () -> Unit,
    onViewAiExplanationClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onLocationSelectorClicked: () -> Unit,
    onOpenDataInfo: () -> Unit,
    modifier: Modifier = Modifier
) {
    val dynamicGreeting = TimeUtils.getDynamicGreeting(userName)

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("home_screen")
    ) {
        // Top Header Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Selector & Brand Name
            Column {
                Text(
                    text = "AIRROUTE 🌿",
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 18.sp,
                    color = DeepNavy,
                    letterSpacing = 0.5.sp
                )

                Spacer(modifier = Modifier.height(2.dp))

                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(CardSurfaceWhite)
                        .clickable { onLocationSelectorClicked() }
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                        .testTag("location_selector_chip"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "📍", fontSize = 12.sp)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = data.locationName,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Change location",
                        tint = DeepNavy,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Header Action Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Testing & Scenarios Control Panel Icon
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldGreenLight)
                        .clickable { onOpenDataInfo() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                        .testTag("data_info_header_button")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Info, contentDescription = "Data Info", tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Data", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                IconButton(onClick = onNotificationClicked) {
                    BadgedBox(
                        badge = {
                            if (unreadAlertCount > 0) {
                                Badge(containerColor = Color(0xFFDC2626)) {
                                    Text(text = "$unreadAlertCount")
                                }
                            }
                        }
                    ) {
                        Icon(Icons.Default.Notifications, contentDescription = "Alerts", tint = DeepNavy)
                    }
                }

                IconButton(onClick = onProfileClicked) {
                    Box(
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(DeepNavy)
                            .padding(6.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = "Profile", tint = Color.White)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // High Pollution Alert Banner
        data.highPollutionWarning?.let { warning ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("high_pollution_warning_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF1F2)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFECDD3))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = "Alert", tint = Color(0xFFDC2626))
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = warning,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF991B1B),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Destination Warning Banner
        data.destinationWarning?.let { destWarning ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 14.dp)
                    .testTag("destination_warning_banner"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "⚠️", fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = destWarning,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF92400E),
                        lineHeight = 16.sp
                    )
                }
            }
        }

        // Time-Aware Greeting
        Text(
            text = dynamicGreeting,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "Know before you go.",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = EmeraldGreen
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "• ${data.lastUpdatedText}",
                fontSize = 12.sp,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Core Real-Time AQI & Pollutants Card
        AirQualityCard(data = data)

        Spacer(modifier = Modifier.height(16.dp))

        // Outdoor Score Card
        OutdoorScoreCard(
            scoreData = data.outdoorScore,
            onWhyScoreClicked = onWhyScoreClicked
        )

        Spacer(modifier = Modifier.height(16.dp))

        // "WHAT IF I WAIT?" Comparative Forecast Card
        WhatIfWaitCard(whatIfData = data.whatIfWait)

        Spacer(modifier = Modifier.height(20.dp))

        // Personal Environmental Exposure Estimator
        data.exposureEstimate?.let { exposure ->
            PersonalExposureCard(exposure = exposure)
            Spacer(modifier = Modifier.height(20.dp))
        }

        // Going Outside? Activity selector
        GoingOutsideSection(
            selectedActivityId = selectedActivityId,
            onActivitySelected = onActivitySelected
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Best Outdoor Window Card
        BestWindowCard(
            data = data,
            onWhyThisTimeClicked = onWhyThisTimeClicked
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Pollution Hotspots & Cleaner Places Card
        HotspotsAndCleanerPlacesCard(
            hotspots = data.hotspots,
            cleanerPlaces = data.cleanerPlaces
        )

        Spacer(modifier = Modifier.height(22.dp))

        // Today's Outlook Horizontal Timeline with Observed vs Forecasted labels
        TodayOutlookTimeline(forecastList = data.hourlyForecast)

        Spacer(modifier = Modifier.height(22.dp))

        // AIRROUTE AI Card
        AirRouteAICard(
            summaryText = data.aiSummaryText,
            confidenceScore = data.aiConfidenceScore,
            trendDirection = data.aiTrendDirection,
            onViewExplanationClicked = onViewAiExplanationClicked
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Data Transparency Footer
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(14.dp))
                .background(Color(0xFFD1FAE5))
                .padding(14.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "🟢", fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = "REAL OBSERVED CPCB & OPEN-METEO DATA",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldGreen
                    )
                    Text(
                        text = "Live air quality readings & weather parameters fetched directly from public monitoring networks in ${data.locationName}.",
                        fontSize = 11.sp,
                        color = TextSecondary,
                        lineHeight = 15.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
