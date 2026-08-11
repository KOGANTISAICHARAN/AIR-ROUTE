package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.theme.OutlineBorder
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary
import com.example.ui.theme.WeatherSkyBlue
import com.example.ui.theme.WeatherSkyBlueLight

@Composable
fun AirQualityCard(
    data: AirQualityStateData,
    modifier: Modifier = Modifier
) {
    val statusColor = Color(data.recommendationState.hexColor)
    val statusBg = Color(data.recommendationState.bgHexColor)

    val animatedStatusColor by animateColorAsState(targetValue = statusColor, animationSpec = tween(400), label = "statusColor")
    val animatedStatusBg by animateColorAsState(targetValue = statusBg, animationSpec = tween(400), label = "statusBg")

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("air_quality_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(22.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "AIR QUALITY NOW",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = animatedStatusColor,
                        letterSpacing = 1.5.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = data.statusLabel,
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Black,
                        color = animatedStatusColor
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = data.statusExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Circular geometric badge
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(androidx.compose.foundation.shape.CircleShape)
                        .background(animatedStatusBg),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "🌿", fontSize = 32.sp)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // PM2.5 & Temp / Weather metrics bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(18.dp))
                    .background(BackgroundOffWhite)
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(
                        text = "PM2.5",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${data.currentPm25} µg/m³",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(OutlineBorder)
                )

                Column {
                    Text(
                        text = "TEMP",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${data.weather.tempC}°C",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .width(1.dp)
                        .height(24.dp)
                        .background(OutlineBorder)
                )

                Column {
                    Text(
                        text = "WIND",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextMuted,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "${data.weather.windKmH} km/h ${data.weather.windDirection}",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }
            }
        }
    }
}

@Composable
private fun WeatherItem(emoji: String, value: String, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(text = emoji, fontSize = 14.sp)
        Spacer(modifier = Modifier.width(4.dp))
        Column {
            Text(
                text = value,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = DeepNavy
            )
            Text(
                text = label,
                fontSize = 10.sp,
                color = TextSecondary
            )
        }
    }
}
