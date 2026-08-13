package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AirQualityStateData
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.utils.AqiUtils

@Composable
fun AirQualityCard(
    data: AirQualityStateData,
    modifier: Modifier = Modifier
) {
    var showAqiExplanationDialog by remember { mutableStateOf(false) }

    val statusColor = Color(data.recommendationState.hexColor)
    val statusBg = Color(data.recommendationState.bgHexColor)

    val animatedStatusColor by animateColorAsState(targetValue = statusColor, animationSpec = tween(400), label = "statusColor")
    val animatedStatusBg by animateColorAsState(targetValue = statusBg, animationSpec = tween(400), label = "statusBg")

    if (showAqiExplanationDialog) {
        AlertDialog(
            onDismissRequest = { showAqiExplanationDialog = false },
            title = { Text("Understanding AQI vs PM2.5 🧪", fontWeight = FontWeight.Bold, color = DeepNavy) },
            text = {
                Column {
                    Text(
                        text = AqiUtils.getExplanationText(),
                        fontSize = 13.sp,
                        color = TextSecondary,
                        lineHeight = 18.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "• PM2.5: Microscopic particles < 2.5 µm that penetrate lungs.\n" +
                                "• AQI (Air Quality Index): Standard 0–500 scale calculated using India CPCB breakpoints.\n" +
                                "• 0–50: Good | 51–100: Satisfactory | 101–200: Moderate | 201–300: Poor | 301–400: Very Poor | 401+: Severe.",
                        fontSize = 12.sp,
                        color = DeepNavy,
                        lineHeight = 16.sp
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = { showAqiExplanationDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen)
                ) {
                    Text("Got it")
                }
            }
        )
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("air_quality_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Live Status & AQI Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "LIVE OBSERVED AIR QUALITY",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = animatedStatusColor,
                            letterSpacing = 1.2.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "AQI vs PM2.5 info",
                            tint = animatedStatusColor,
                            modifier = Modifier
                                .size(16.dp)
                                .clickable { showAqiExplanationDialog = true }
                                .testTag("aqi_info_button")
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Row(verticalAlignment = Alignment.Bottom) {
                        Text(
                            text = "${data.currentAqi}",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = animatedStatusColor
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "AQI (${data.aqiCategory.title})",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = animatedStatusColor,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )
                    }

                    Text(
                        text = data.statusExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Box(
                    modifier = Modifier
                        .size(68.dp)
                        .clip(CircleShape)
                        .background(animatedStatusBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌿", fontSize = 30.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Primary Pollutants Row (PM2.5, PM10, NO2, O3)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(16.dp))
                    .background(BackgroundOffWhite)
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                PollutantMetricItem(title = "PM2.5", value = "${data.currentPm25} µg/m³", highlight = true)
                VerticalDivider()
                PollutantMetricItem(title = "PM10", value = "${data.currentPm10} µg/m³")
                VerticalDivider()
                PollutantMetricItem(title = "NO2", value = "${data.currentNo2} µg/m³")
                VerticalDivider()
                PollutantMetricItem(title = "O3", value = "${data.currentO3} µg/m³")
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Weather & Wind Summary Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = data.weather.weatherIcon, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "${data.weather.tempC}°C • ${data.weather.weatherCondition}",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                Text(
                    text = "💨 ${data.weather.windKmH} km/h ${data.weather.windDirection}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Data Source & Freshness Footer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Source: ${data.dataSourceName}",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextMuted
                )
                Text(
                    text = data.lastUpdatedText,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = EmeraldGreen
                )
            }
        }
    }
}

@Composable
private fun PollutantMetricItem(title: String, value: String, highlight: Boolean = false) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 0.5.sp
        )
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (highlight) FontWeight.Black else FontWeight.Bold,
            color = if (highlight) EmeraldGreen else DeepNavy
        )
    }
}

@Composable
private fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(20.dp)
            .background(OutlineBorder)
    )
}
