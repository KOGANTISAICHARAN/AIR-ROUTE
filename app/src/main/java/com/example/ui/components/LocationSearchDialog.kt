package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.MyLocation
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextSecondary

@Composable
fun LocationSearchDialog(
    currentLocation: String,
    onLocationSelected: (String) -> Unit,
    onUseGpsLocation: () -> Unit,
    onDismiss: () -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }

    val presetLocations = listOf(
        "Hyderabad, Telangana",
        "Gachibowli, Hyderabad",
        "Hitech City, Hyderabad",
        "Kukatpally, Hyderabad",
        "Bengaluru, Karnataka",
        "Indiranagar, Bengaluru",
        "New Delhi, NCR",
        "Dwarka, Delhi",
        "Mumbai, Maharashtra",
        "Bandra, Mumbai",
        "London, UK",
        "Tokyo, Japan",
        "New York, USA"
    )

    val filteredLocations = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            presetLocations
        } else {
            val matched = presetLocations.filter { it.contains(searchQuery, ignoreCase = true) }
            if (matched.isEmpty()) listOf(searchQuery.trim()) else matched
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(24.dp),
        containerColor = CardSurfaceWhite,
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Select Location 📍", fontWeight = FontWeight.Bold, color = DeepNavy, fontSize = 18.sp)
                IconButton(onClick = onDismiss) {
                    Icon(Icons.Default.Close, contentDescription = "Close", tint = DeepNavy)
                }
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                // GPS Button
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = EmeraldGreenLight),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(14.dp))
                        .clickable {
                            onUseGpsLocation()
                            onDismiss()
                        }
                        .testTag("use_gps_location_card")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(Icons.Default.MyLocation, contentDescription = "GPS", tint = EmeraldGreen)
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Use Current GPS Location", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = DeepNavy)
                            Text("Fetch live station air quality for exact coordinates", fontSize = 11.sp, color = TextSecondary)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Search Bar
                OutlinedTextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    placeholder = { Text("Search city or neighborhood...") },
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search", tint = DeepNavy) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("location_search_input")
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text("Available Cities & Neighborhoods", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextSecondary)

                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(modifier = Modifier.height(200.dp)) {
                    items(filteredLocations) { location ->
                        val isSelected = location.equals(currentLocation, ignoreCase = true)

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) EmeraldGreenLight else Color.Transparent)
                                .clickable {
                                    onLocationSelected(location)
                                    onDismiss()
                                }
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = if (isSelected) EmeraldGreen else TextSecondary
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = location,
                                fontWeight = if (isSelected) FontWeight.ExtraBold else FontWeight.Medium,
                                color = if (isSelected) EmeraldGreen else DeepNavy,
                                fontSize = 13.sp
                            )
                        }
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                    }
                }
            }
        },
        confirmButton = {}
    )
}
