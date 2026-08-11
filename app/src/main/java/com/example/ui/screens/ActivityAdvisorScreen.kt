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
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextSecondary

@Composable
fun ActivityAdvisorScreen(
    data: AirQualityStateData,
    preferredActivityId: String,
    onSelectActivity: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // Sort activities so user's preferred activity comes first
    val sortedActivities = data.activitiesAdvice.sortedByDescending { it.id == preferredActivityId }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("activity_advisor_screen")
    ) {
        Text(
            text = "What are you planning?",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavy
        )

        Text(
            text = "Personalized air-quality advice for your specific outdoor activities.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(18.dp))

        sortedActivities.forEach { activity ->
            val color = Color(activity.recommendationState.hexColor)
            val bgColor = Color(activity.recommendationState.bgHexColor)
            val isPreferred = activity.id == preferredActivityId

            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                border = if (isPreferred) androidx.compose.foundation.BorderStroke(2.dp, Color(0xFF059669)) else null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 6.dp)
                    .testTag("activity_card_${activity.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = activity.iconEmoji, fontSize = 28.sp)
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = activity.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp,
                                        color = DeepNavy
                                    )
                                    if (isPreferred) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(8.dp))
                                                .background(Color(0xFFD1FAE5))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        ) {
                                            Text(
                                                text = "FAVORITE",
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color(0xFF059669)
                                            )
                                        }
                                    }
                                }
                                Text(
                                    text = activity.bestWindow,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF0284C7)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(bgColor)
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = activity.statusLabel,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = color
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = activity.adviceText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepNavy.copy(alpha = 0.85f),
                        lineHeight = 20.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
    }
}
