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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import com.example.ui.components.TodayOutlookTimeline
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    userName: String,
    data: AirQualityStateData,
    selectedActivityId: String,
    unreadAlertCount: Int,
    onActivitySelected: (String) -> Unit,
    onWhyThisTimeClicked: () -> Unit,
    onViewAiExplanationClicked: () -> Unit,
    onNotificationClicked: () -> Unit,
    onProfileClicked: () -> Unit,
    onLocationSelectorClicked: () -> Unit,
    onOpenDemoPanel: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("home_screen")
    ) {
        // Top Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Location Chip & App Name
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
                        .padding(horizontal = 8.dp, vertical = 4.dp),
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
                        contentDescription = "Select location",
                        tint = DeepNavy,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            }

            // Right Action Icons: Demo Hackathon Switcher, Notification, Profile
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Hackathon evaluator demo badge button
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldGreenLight)
                        .clickable { onOpenDemoPanel() }
                        .padding(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Science, contentDescription = "Demo Mode", tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = "Demo", fontSize = 11.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldGreen)
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

        Spacer(modifier = Modifier.height(18.dp))

        // Greeting
        Text(
            text = "Good morning, $userName! 👋",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )

        Text(
            text = "Plan your outdoor time around the air.",
            fontSize = 14.sp,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Current Air Quality Card
        AirQualityCard(data = data)

        Spacer(modifier = Modifier.height(20.dp))

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

        // Today's Outlook Horizontal Timeline
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

        // Outdoor Tip Card
        Card(
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.Info, contentDescription = null, tint = EmeraldGreen)
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(text = "Outdoor tip", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                    Text(
                        text = "Air quality can change throughout the day. Check the forecast before prolonged outdoor activity.",
                        fontSize = 12.sp,
                        color = TextSecondary,
                        lineHeight = 16.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Transparency Banner
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFFEF3C7))
                .padding(12.dp)
        ) {
            Text(
                text = "Prototype • Demo Data: AIRROUTE combines historical PM2.5 observations and weather data for short-term forecasting demonstration.",
                fontSize = 11.sp,
                color = Color(0xFFD97706),
                fontWeight = FontWeight.Medium,
                lineHeight = 15.sp
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}
