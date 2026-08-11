package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.data.AirQualityStateData
import com.example.ui.components.MapViewComponent
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextSecondary

@Composable
fun MapScreen(
    data: AirQualityStateData,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("map_screen")
    ) {
        Text(
            text = "Air Quality Map",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavy
        )

        Text(
            text = "Explore simulated neighborhood PM2.5 monitoring points across ${data.locationName}.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary
        )

        Spacer(modifier = Modifier.height(16.dp))

        MapViewComponent(
            locationName = data.locationName,
            mapNodes = data.mapNodes
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
