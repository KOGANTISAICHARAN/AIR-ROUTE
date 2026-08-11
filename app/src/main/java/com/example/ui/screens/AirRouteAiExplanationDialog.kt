package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
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
import androidx.compose.ui.window.Dialog
import com.example.data.AirQualityStateData
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextSecondary

@Composable
fun AirRouteAiExplanationDialog(
    data: AirQualityStateData,
    onDismiss: () -> Unit
) {
    var userQuestion by remember { mutableStateOf("") }
    var aiAnswer by remember { mutableStateOf<String?>(null) }
    var isThinking by remember { mutableStateOf(false) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = CardSurfaceWhite,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("ai_explanation_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "🤖", fontSize = 22.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "AIRROUTE AI Explanation",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.ExtraBold,
                                color = DeepNavy
                            )
                            Text(
                                text = "Human-centered forecast reasoning",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Close")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Best Window Banner
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(16.dp))
                        .background(EmeraldGreenLight)
                        .padding(14.dp)
                ) {
                    Column {
                        Text(
                            text = "RECOMMENDED WINDOW",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EmeraldGreen
                        )
                        Text(
                            text = data.bestWindowTime,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Human Plain Language Breakdown
                Text(
                    text = "Why this time is recommended:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = data.bestWindowExplanation,
                    style = MaterialTheme.typography.bodyMedium,
                    color = DeepNavy.copy(alpha = 0.9f),
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                HorizontalDivider(color = Color(0xFFE2E8F0))

                Spacer(modifier = Modifier.height(16.dp))

                // Key Forecast Factors
                Text(
                    text = "Environmental Factors Considered:",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = DeepNavy
                )

                Spacer(modifier = Modifier.height(10.dp))

                FactorRow("💨 Atmospheric Dispersion", "${data.weather.windKmH} km/h ${data.weather.windDirection} wind clearing fine dust.")
                FactorRow("🌡️ Inversion Layer", "${data.weather.tempC}°C surface air with ${data.weather.humidityPercent}% relative humidity.")
                FactorRow("🚗 Traffic Density Trend", "Peak evening vehicular emissions starting at 5:30 PM.")
                FactorRow("🎯 Confidence Score", "${data.aiConfidenceScore}% match based on historical PM2.5 observations.")

                Spacer(modifier = Modifier.height(18.dp))

                // Ask AIRROUTE AI
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFF8FAF8)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp)
                    ) {
                        Text(
                            text = "Have a question about the forecast?",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = DeepNavy
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            value = userQuestion,
                            onValueChange = { userQuestion = it },
                            placeholder = { Text("e.g., Should I wear a mask?", fontSize = 12.sp) },
                            trailingIcon = {
                                IconButton(
                                    onClick = {
                                        if (userQuestion.isNotBlank()) {
                                            isThinking = true
                                            aiAnswer = when {
                                                userQuestion.contains("mask", ignoreCase = true) ->
                                                    "An N95 mask is beneficial if PM2.5 exceeds 100 µg/m³. Right now PM2.5 is ${data.currentPm25} µg/m³."
                                                userQuestion.contains("asthma", ignoreCase = true) ->
                                                    "Sensitive individuals with asthma should keep reliever inhalers handy and prefer windows when PM2.5 is below 50 µg/m³."
                                                else ->
                                                    "Based on current trends in ${data.locationName}, the air is ${data.statusLabel.lowercase()} with PM2.5 around ${data.currentPm25} µg/m³. The best outdoor period remains ${data.bestWindowTime}."
                                            }
                                            isThinking = false
                                        }
                                    }
                                ) {
                                    Icon(Icons.Default.Send, contentDescription = "Send", tint = EmeraldGreen)
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ai_explanation_question_input")
                        )

                        aiAnswer?.let { answer ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(EmeraldGreenLight)
                                    .padding(12.dp)
                            ) {
                                Text(
                                    text = answer,
                                    fontSize = 12.sp,
                                    color = DeepNavy,
                                    fontWeight = FontWeight.Medium,
                                    lineHeight = 18.sp
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Disclaimer
                Text(
                    text = "Disclaimer: AIRROUTE provides statistical predictions for planning purposes only and does not provide medical advice or guaranteed safety.",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onDismiss,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = DeepNavy),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Got it", fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun FactorRow(title: String, desc: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.Top
    ) {
        Text(text = "• ", fontWeight = FontWeight.Bold, color = EmeraldGreen)
        Column {
            Text(text = title, fontWeight = FontWeight.Bold, fontSize = 12.sp, color = DeepNavy)
            Text(text = desc, fontSize = 11.sp, color = TextSecondary)
        }
    }
}
