package com.example.ui.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.HourglassTop
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RecommendationState
import com.example.data.WhatIfOption
import com.example.data.WhatIfWaitData
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextSecondary

@Composable
fun WhatIfWaitCard(
    whatIfData: WhatIfWaitData,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = modifier
            .fillMaxWidth()
            .testTag("what_if_wait_card")
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    imageVector = Icons.Default.HourglassTop,
                    contentDescription = null,
                    tint = DeepNavy,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "WHAT IF I WAIT?",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepNavy,
                    letterSpacing = 0.8.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "AIRROUTE 4-hour predictive comparative model",
                fontSize = 11.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                WhatIfOptionItem(
                    option = whatIfData.goNow,
                    isBestChoice = true,
                    modifier = Modifier.weight(1f)
                )

                WhatIfOptionItem(
                    option = whatIfData.wait2h,
                    isBestChoice = false,
                    modifier = Modifier.weight(1f)
                )

                WhatIfOptionItem(
                    option = whatIfData.wait4h,
                    isBestChoice = false,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF1F5F9))
                    .padding(12.dp)
            ) {
                Text(
                    text = "💡 Decision Support: ${whatIfData.decisionAdvice}",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = DeepNavy
                )
            }
        }
    }
}

@Composable
private fun WhatIfOptionItem(
    option: WhatIfOption,
    isBestChoice: Boolean,
    modifier: Modifier = Modifier
) {
    val stateColor = when (option.recommendationState) {
        RecommendationState.GOOD_TO_GO -> EmeraldGreen
        RecommendationState.CONSIDER_WAITING -> Color(0xFFD97706)
        RecommendationState.BETTER_INDOORS -> Color(0xFFDC2626)
    }

    val stateBg = when (option.recommendationState) {
        RecommendationState.GOOD_TO_GO -> Color(0xFFD1FAE5)
        RecommendationState.CONSIDER_WAITING -> Color(0xFFFEF3C7)
        RecommendationState.BETTER_INDOORS -> Color(0xFFFEE2E2)
    }

    val borderColor = if (isBestChoice) EmeraldGreen else Color(0xFFE2E8F0)

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(16.dp))
            .background(if (isBestChoice) Color(0xFFF0FDF4) else Color(0xFFFAFAFA))
            .border(
                width = if (isBestChoice) 1.5.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .padding(10.dp)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = option.timeLabel,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = DeepNavy,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${option.pm25}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = stateColor
            )

            Text(
                text = "µg/m³",
                fontSize = 9.sp,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(stateBg)
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = option.statusLabel,
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = stateColor
                )
            }
        }
    }
}
