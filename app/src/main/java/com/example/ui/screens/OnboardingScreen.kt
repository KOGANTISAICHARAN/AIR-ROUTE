package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
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
import com.example.data.DemoDataRepository
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextSecondary

@Composable
fun OnboardingScreen(
    onOnboardingComplete: (String, List<String>, String) -> Unit,
    modifier: Modifier = Modifier
) {
    var step by remember { mutableIntStateOf(1) }

    var selectedLocation by remember { mutableStateOf("Hyderabad, Telangana") }
    val selectedActivities = remember { mutableStateListOf("Running", "Walking") }
    var selectedNotificationTime by remember { mutableStateOf("Morning") }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .testTag("onboarding_screen")
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            // Header Progress
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "AIRROUTE 🌿", fontWeight = FontWeight.Bold, color = DeepNavy)
                    Text(text = "Step $step of 3", fontSize = 12.sp, color = TextSecondary, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(8.dp))

                LinearProgressIndicator(
                    progress = { step / 3f },
                    color = EmeraldGreen,
                    trackColor = Color(0xFFE2E8F0),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                )
            }

            // Step Content
            when (step) {
                1 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "📍", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "Where are you usually?",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                        Text(
                            text = "AIRROUTE provides hyperlocal PM2.5 forecasts for your primary city.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        DemoDataRepository.AVAILABLE_LOCATIONS.forEach { loc ->
                            val isSelected = loc == selectedLocation
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) EmeraldGreenLight else CardSurfaceWhite
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, EmeraldGreen) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedLocation = loc }
                                    .testTag("location_option_$loc")
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = if (isSelected) EmeraldGreen else TextSecondary)
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = loc,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }

                2 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🏃", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "What do you usually do outdoors?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                        Text(
                            text = "Select all that apply. We'll prioritize outdoor windows for these.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val activities = listOf(
                            Pair("Running", "🏃"),
                            Pair("Walking", "🚶"),
                            Pair("Cycling", "🚴"),
                            Pair("Outdoor Work", "🌳"),
                            Pair("General Outdoor Activity", "☀️")
                        )

                        activities.forEach { (act, emoji) ->
                            val isSelected = selectedActivities.contains(act)
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) EmeraldGreenLight else CardSurfaceWhite
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, EmeraldGreen) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable {
                                        if (isSelected) selectedActivities.remove(act) else selectedActivities.add(act)
                                    }
                                    .testTag("activity_option_$act")
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(text = emoji, fontSize = 20.sp)
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = act,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = DeepNavy,
                                            fontSize = 15.sp
                                        )
                                    }
                                    if (isSelected) {
                                        Icon(Icons.Default.Check, contentDescription = "Selected", tint = EmeraldGreen)
                                    }
                                }
                            }
                        }
                    }
                }

                3 -> {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "🔔", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "When should AIRROUTE notify you?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = DeepNavy
                        )
                        Text(
                            text = "Choose your preferred time to receive daily outdoor air quality forecasts.",
                            fontSize = 13.sp,
                            color = TextSecondary,
                            modifier = Modifier.padding(top = 4.dp)
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        val options = listOf("Morning", "Afternoon", "Evening", "All Day")

                        options.forEach { opt ->
                            val isSelected = opt == selectedNotificationTime
                            Card(
                                shape = RoundedCornerShape(16.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = if (isSelected) EmeraldGreenLight else CardSurfaceWhite
                                ),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, EmeraldGreen) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(16.dp))
                                    .clickable { selectedNotificationTime = opt }
                                    .testTag("notification_time_option_$opt")
                            ) {
                                Row(
                                    modifier = Modifier.padding(16.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = when(opt) {
                                            "Morning" -> "🌅"
                                            "Afternoon" -> "☀️"
                                            "Evening" -> "🌆"
                                            else -> "🔔"
                                        },
                                        fontSize = 20.sp
                                    )
                                    Spacer(modifier = Modifier.width(12.dp))
                                    Text(
                                        text = opt,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = DeepNavy,
                                        fontSize = 15.sp
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Bottom Navigation Controls
            Column(modifier = Modifier.fillMaxWidth()) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (step > 1) {
                        TextButton(onClick = { step-- }) {
                            Text("Back", color = TextSecondary, fontWeight = FontWeight.Bold)
                        }
                    } else {
                        Spacer(modifier = Modifier.width(1.dp))
                    }

                    Button(
                        onClick = {
                            if (step < 3) {
                                step++
                            } else {
                                onOnboardingComplete(selectedLocation, selectedActivities.toList(), selectedNotificationTime)
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                        modifier = Modifier.testTag("onboarding_next_button")
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (step == 3) "Start AIRROUTE" else "Next",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        }
    }
}
