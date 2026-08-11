package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import com.example.ui.components.ForecastChart
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextSecondary

@Composable
fun ForecastScreen(
    data: AirQualityStateData,
    modifier: Modifier = Modifier
) {
    var is24hView by remember { mutableStateOf(false) }
    val activeForecast = if (is24hView) data.forecast24h else data.hourlyForecast

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("forecast_screen")
    ) {
        Text(
            text = "Today's Air Quality Forecast",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavy
        )

        Text(
            text = "Short-term AI predictions based on historical PM2.5 observations and weather models.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Range Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Start
        ) {
            FilterChip(
                selected = !is24hView,
                onClick = { is24hView = false },
                label = { Text("Next 6 Hours", fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepNavy,
                    selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("forecast_6h_chip")
            )

            Spacer(modifier = Modifier.width(10.dp))

            FilterChip(
                selected = is24hView,
                onClick = { is24hView = true },
                label = { Text("Next 24 Hours", fontWeight = FontWeight.Bold) },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = DeepNavy,
                    selectedLabelColor = Color.White
                ),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier.testTag("forecast_24h_chip")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Interactive Forecast Chart
        ForecastChart(hourlyForecast = activeForecast)

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Hourly Prediction Breakdown",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )

        Spacer(modifier = Modifier.height(12.dp))

        activeForecast.forEach { item ->
            val color = Color(item.recommendationState.hexColor)
            val bgColor = Color(item.recommendationState.bgHexColor)

            Card(
                shape = RoundedCornerShape(18.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = item.timeLabel,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 16.sp,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            if (!item.isPredicted) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(Color(0xFFE2E8F0))
                                        .padding(horizontal = 6.dp, vertical = 2.dp)
                                ) {
                                    Text(text = "LIVE", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = DeepNavy)
                                }
                            }
                        }
                        Text(
                            text = "${item.pm25} µg/m³ PM2.5",
                            fontSize = 13.sp,
                            color = TextSecondary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(bgColor)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = item.statusLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = color
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
