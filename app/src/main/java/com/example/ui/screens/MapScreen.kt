package com.example.ui.screens

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.AirQualityStateData
import com.example.data.RouteOption
import com.example.ui.components.MapViewComponent
import com.example.ui.theme.BackgroundOffWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextSecondary

@Composable
fun MapScreen(
    data: AirQualityStateData,
    destinationName: String = "KBR National Park",
    onDestinationChanged: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var destinationInput by remember(destinationName) { mutableStateOf(destinationName) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(BackgroundOffWhite)
            .verticalScroll(rememberScrollState())
            .padding(18.dp)
            .testTag("map_screen")
    ) {
        Text(
            text = "Cleaner Air Route Planner 🌿",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.ExtraBold,
            color = DeepNavy
        )

        Text(
            text = "Real-time PM2.5 monitoring stations & air-quality-aware navigation across ${data.locationName}.",
            style = MaterialTheme.typography.bodyMedium,
            color = TextSecondary,
            fontSize = 12.sp
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Destination Input Box
        OutlinedTextField(
            value = destinationInput,
            onValueChange = {
                destinationInput = it
                onDestinationChanged(it)
            },
            label = { Text("Set Destination / Neighborhood") },
            placeholder = { Text("e.g. KBR National Park, Gachibowli, Kukatpally") },
            singleLine = true,
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("destination_input_field")
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Destination Air Quality Warning if applicable
        data.destinationWarning?.let { warning ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 12.dp)
                    .testTag("map_destination_warning"),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFFEF3C7)),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFDE68A))
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Warning, contentDescription = null, tint = Color(0xFFB45309))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = warning, fontSize = 12.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF92400E))
                }
            }
        }

        MapViewComponent(
            locationName = data.locationName,
            mapNodes = data.mapNodes,
            routeOptions = data.routeOptions,
            destinationName = destinationInput,
            onDestinationChanged = onDestinationChanged
        )

        Spacer(modifier = Modifier.height(20.dp))
    }
}
