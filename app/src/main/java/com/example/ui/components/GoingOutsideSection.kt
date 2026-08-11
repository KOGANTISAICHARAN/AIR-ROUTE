package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

data class ActivityItemData(
    val id: String,
    val title: String,
    val emoji: String
)

val DEFAULT_ACTIVITIES = listOf(
    ActivityItemData("running", "Running", "🏃"),
    ActivityItemData("walking", "Walking", "🚶"),
    ActivityItemData("cycling", "Cycling", "🚴"),
    ActivityItemData("work", "Outdoor Work", "🌳"),
    ActivityItemData("outing", "Just Going Out", "☀️")
)

@Composable
fun GoingOutsideSection(
    selectedActivityId: String,
    onActivitySelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = "GOING OUTSIDE?",
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = TextMuted,
            letterSpacing = 1.2.sp,
            modifier = Modifier.padding(horizontal = 4.dp)
        )

        Spacer(modifier = Modifier.height(10.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            contentPadding = PaddingValues(horizontal = 2.dp)
        ) {
            items(DEFAULT_ACTIVITIES, key = { it.id }) { activity ->
                val isSelected = activity.id == selectedActivityId

                val bgColor by animateColorAsState(
                    targetValue = if (isSelected) DeepNavy else CardSurfaceWhite,
                    label = "actBg"
                )
                val textColor by animateColorAsState(
                    targetValue = if (isSelected) Color.White else DeepNavy,
                    label = "actText"
                )

                Card(
                    modifier = Modifier
                        .width(96.dp)
                        .height(96.dp)
                        .clip(RoundedCornerShape(28.dp))
                        .clickable { onActivitySelected(activity.id) }
                        .testTag("activity_chip_${activity.id}"),
                    shape = RoundedCornerShape(28.dp),
                    colors = CardDefaults.cardColors(containerColor = bgColor),
                    border = if (!isSelected) androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)) else null,
                    elevation = CardDefaults.cardElevation(defaultElevation = if (isSelected) 4.dp else 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .padding(14.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.SpaceBetween,
                        horizontalAlignment = Alignment.Start
                    ) {
                        Text(text = activity.emoji, fontSize = 24.sp)
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = activity.title,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = textColor,
                            maxLines = 1
                        )
                    }
                }
            }
        }
    }
}
