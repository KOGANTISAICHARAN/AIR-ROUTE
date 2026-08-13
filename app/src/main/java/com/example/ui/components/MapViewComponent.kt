package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Canvas
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Directions
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Navigation
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MapLocationNode
import com.example.data.RouteOption
import com.example.data.RouteType
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.EmeraldGreenLight
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

@Composable
fun MapViewComponent(
    locationName: String,
    mapNodes: List<MapLocationNode>,
    routeOptions: List<RouteOption> = emptyList(),
    destinationName: String = "KBR National Park",
    onDestinationChanged: (String) -> Unit = {},
    onSelectRoute: (RouteOption) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedNode by remember(mapNodes) { mutableStateOf<MapLocationNode?>(mapNodes.firstOrNull()) }
    var selectedRoute by remember(routeOptions) { mutableStateOf(routeOptions.firstOrNull { it.isRecommended } ?: routeOptions.firstOrNull()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("map_view_card"),
        shape = RoundedCornerShape(28.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp)
        ) {
            // Header with Destination Selector
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "REAL-TIME AIR ROUTE & STATIONS",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = EmeraldGreen,
                        letterSpacing = 1.2.sp
                    )
                    Text(
                        text = "$locationName → $destinationName",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .background(EmeraldGreenLight)
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "CPCB Live Network",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldGreen
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Prominent "FIND CLEANER ROUTE" Button
            Button(
                onClick = {
                    val cleaner = routeOptions.firstOrNull { it.type == RouteType.CLEANER_AIR } ?: routeOptions.firstOrNull()
                    cleaner?.let {
                        selectedRoute = it
                        onSelectRoute(it)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldGreen),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("find_cleaner_route_button")
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("🌿 FIND CLEANER ROUTE", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.Default.Navigation, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Map Visual Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(22.dp))
                    .background(Color(0xFFE2E8F0).copy(alpha = 0.6f))
            ) {
                // Background Vector Map Canvas (Lake, Road grid, Color-coded Route Polylines)
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Park / Lake geometry
                    val lakePath = Path().apply {
                        moveTo(w * 0.55f, h * 0.38f)
                        cubicTo(w * 0.68f, h * 0.32f, w * 0.78f, h * 0.58f, w * 0.60f, h * 0.68f)
                        cubicTo(w * 0.48f, h * 0.72f, w * 0.42f, h * 0.52f, w * 0.55f, h * 0.38f)
                        close()
                    }
                    drawPath(lakePath, color = Color(0xFFBAE6FD))

                    // Base road networks
                    drawLine(Color.White, Offset(0f, h * 0.32f), Offset(w, h * 0.32f), strokeWidth = 8f)
                    drawLine(Color.White, Offset(0f, h * 0.68f), Offset(w, h * 0.68f), strokeWidth = 10f)
                    drawLine(Color.White, Offset(w * 0.35f, 0f), Offset(w * 0.35f, h), strokeWidth = 8f)
                    drawLine(Color.White, Offset(w * 0.72f, 0f), Offset(w * 0.72f, h), strokeWidth = 8f)

                    // Draw Route Polyline based on selected route
                    val isCleaner = selectedRoute?.type == RouteType.CLEANER_AIR
                    val isFastest = selectedRoute?.type == RouteType.FASTEST

                    val routeColor = when {
                        isCleaner -> Color(0xFF059669) // Emerald
                        isFastest -> Color(0xFFDC2626) // Red/High exposure
                        else -> Color(0xFFD97706) // Orange/Balanced
                    }

                    val routePath = Path().apply {
                        moveTo(w * 0.15f, h * 0.75f) // Origin
                        if (isCleaner) {
                            // Scenic green park bypass
                            cubicTo(w * 0.25f, h * 0.30f, w * 0.50f, h * 0.20f, w * 0.82f, h * 0.35f)
                        } else if (isFastest) {
                            // Direct congested highway
                            lineTo(w * 0.52f, h * 0.68f)
                            lineTo(w * 0.82f, h * 0.35f)
                        } else {
                            // Balanced route
                            cubicTo(w * 0.35f, h * 0.55f, w * 0.60f, h * 0.45f, w * 0.82f, h * 0.35f)
                        }
                    }

                    drawPath(
                        path = routePath,
                        color = routeColor,
                        style = Stroke(width = 12f)
                    )

                    // Origin Pin (Green)
                    drawCircle(Color(0xFF059669), radius = 16f, center = Offset(w * 0.15f, h * 0.75f))
                    drawCircle(Color.White, radius = 6f, center = Offset(w * 0.15f, h * 0.75f))

                    // Destination Pin (Navy)
                    drawCircle(Color(0xFF0F172A), radius = 18f, center = Offset(w * 0.82f, h * 0.35f))
                    drawCircle(Color(0xFF38BDF8), radius = 8f, center = Offset(w * 0.82f, h * 0.35f))
                }

                // Station Pins
                mapNodes.forEach { node ->
                    val isSelected = selectedNode?.id == node.id
                    val color = Color(node.recommendationState.hexColor)

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                start = (node.xRatio * 260).dp,
                                top = (node.yRatio * 180).dp
                            )
                            .clickable { selectedNode = node }
                            .testTag("map_marker_${node.id}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Box(
                                modifier = Modifier
                                    .clip(CircleShape)
                                    .background(color)
                                    .border(2.dp, Color.White, CircleShape)
                                    .padding(horizontal = 7.dp, vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${node.aqi}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 10.sp,
                                    color = Color.White
                                )
                            }
                            Text(
                                text = node.name.split(" ").firstOrNull() ?: "",
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                        }
                    }
                }

                // Selected Node Info Card Overlay
                selectedNode?.let { node ->
                    val color = Color(node.recommendationState.hexColor)
                    val bgColor = Color(node.recommendationState.bgHexColor)

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(10.dp)
                            .clip(RoundedCornerShape(16.dp))
                            .background(CardSurfaceWhite)
                            .border(1.dp, color.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = node.name,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${node.coverageType} • ${node.lastUpdated}",
                                    fontSize = 10.sp,
                                    color = TextMuted
                                )
                                Text(
                                    text = node.quickAdvice,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(bgColor)
                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "${node.aqi} AQI",
                                        fontWeight = FontWeight.Black,
                                        fontSize = 13.sp,
                                        color = color
                                    )
                                    Text(
                                        text = "PM2.5: ${node.pm25} µg",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 9.sp,
                                        color = color
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Route Cards List
            if (routeOptions.isNotEmpty()) {
                Text(
                    text = "Calculated Routes (${locationName} → ${destinationName})",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary
                )

                Spacer(modifier = Modifier.height(8.dp))

                routeOptions.forEach { route ->
                    val isSelected = selectedRoute?.id == route.id

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable {
                                selectedRoute = route
                                onSelectRoute(route)
                            }
                            .testTag("route_option_${route.id}"),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isSelected) EmeraldGreenLight else Color(0xFFF8FAFC)
                        ),
                        border = androidx.compose.foundation.BorderStroke(
                            1.dp,
                            if (isSelected) EmeraldGreen else Color(0xFFE2E8F0)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = route.title,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = if (isSelected) EmeraldGreen else DeepNavy
                                    )
                                    if (route.type == RouteType.CLEANER_AIR) {
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "RECOMMENDED",
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.ExtraBold,
                                            color = EmeraldGreen,
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(EmeraldGreen.copy(alpha = 0.15f))
                                                .padding(horizontal = 6.dp, vertical = 2.dp)
                                        )
                                    }
                                }
                                Text(
                                    text = route.recommendationText,
                                    fontSize = 11.sp,
                                    color = TextSecondary,
                                    lineHeight = 15.sp
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${route.travelTimeMins} min",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 15.sp,
                                    color = DeepNavy
                                )
                                Text(
                                    text = "${route.distanceKm} km • ${route.averageAqi} AQI",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (route.type == RouteType.CLEANER_AIR) EmeraldGreen else TextMuted
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
