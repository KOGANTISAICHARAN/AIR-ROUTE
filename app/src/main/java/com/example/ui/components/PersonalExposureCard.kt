package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import com.example.data.PersonalExposureEstimate
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun PersonalExposureCard(
    exposure: PersonalExposureEstimate,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("personal_exposure_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0))
    ) {
        Column(modifier = Modifier.padding(18.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "📊 Personal Environmental Exposure",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    color = DeepNavy
                )
                Text(
                    text = exposure.exposureCategory,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color(exposure.colorHex),
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(exposure.colorHex).copy(alpha = 0.12f))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color(0xFFF8FAFC))
                    .padding(14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text(text = "ESTIMATED PM2.5 INHALATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(
                        text = "${exposure.totalInhaledPm25Ug} µg",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(exposure.colorHex)
                    )
                }

                Column(horizontalAlignment = Alignment.End) {
                    Text(text = "PLANNED DURATION", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextMuted)
                    Text(
                        text = "${exposure.durationMins} mins ${exposure.activityName}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = exposure.comparisonSubtext,
                fontSize = 11.sp,
                color = TextSecondary,
                lineHeight = 15.sp
            )
        }
    }
}
