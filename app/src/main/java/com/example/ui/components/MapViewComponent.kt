package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.MapLocationNode
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.TextSecondary

@Composable
fun MapViewComponent(
    locationName: String,
    mapNodes: List<MapLocationNode>,
    modifier: Modifier = Modifier
) {
    var selectedNode by remember(mapNodes) { mutableStateOf<MapLocationNode?>(mapNodes.firstOrNull()) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("map_view_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Map Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = DeepNavy
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = locationName,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = DeepNavy
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(Color(0xFFFEF3C7))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Demo Map Data",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFFD97706)
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Map Visual Canvas Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(260.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xFFE2E8F0).copy(alpha = 0.5f))
            ) {
                // Background Stylized Vector Roads & Lakes
                Canvas(modifier = Modifier.fillMaxSize()) {
                    val w = size.width
                    val h = size.height

                    // Hussain Sagar lake blob in center
                    val lakePath = Path().apply {
                        moveTo(w * 0.55f, h * 0.40f)
                        cubicTo(w * 0.65f, h * 0.35f, w * 0.75f, h * 0.55f, w * 0.62f, h * 0.65f)
                        cubicTo(w * 0.50f, h * 0.70f, w * 0.45f, h * 0.50f, w * 0.55f, h * 0.40f)
                        close()
                    }
                    drawPath(lakePath, color = Color(0xFFBAE6FD))

                    // Road line grid
                    drawLine(Color.White, Offset(0f, h * 0.3f), Offset(w, h * 0.3f), strokeWidth = 8f)
                    drawLine(Color.White, Offset(0f, h * 0.65f), Offset(w, h * 0.65f), strokeWidth = 10f)
                    drawLine(Color.White, Offset(w * 0.4f, 0f), Offset(w * 0.4f, h), strokeWidth = 8f)
                    drawLine(Color.White, Offset(w * 0.75f, 0f), Offset(w * 0.75f, h), strokeWidth = 8f)
                }

                // Map Location Pins
                mapNodes.forEach { node ->
                    val isSelected = selectedNode?.id == node.id
                    val color = Color(node.recommendationState.hexColor)

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopStart)
                            .padding(
                                start = (node.xRatio * 280).dp,
                                top = (node.yRatio * 200).dp
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
                                    .padding(horizontal = 8.dp, vertical = 4.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${node.pm25}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 11.sp,
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

                // Selected Node Info Popup Box Overlay
                selectedNode?.let { node ->
                    val color = Color(node.recommendationState.hexColor)
                    val bgColor = Color(node.recommendationState.bgHexColor)

                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(12.dp)
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = node.name,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = DeepNavy
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "(${node.area})",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                                Text(
                                    text = node.quickAdvice,
                                    fontSize = 12.sp,
                                    color = DeepNavy.copy(alpha = 0.8f)
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
                                        text = "${node.pm25} µg/m³",
                                        fontWeight = FontWeight.ExtraBold,
                                        fontSize = 13.sp,
                                        color = color
                                    )
                                    Text(
                                        text = node.statusLabel,
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

            Spacer(modifier = Modifier.height(12.dp))

            // Map Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceAround,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem("🟢 GOOD", Color(0xFF059669))
                LegendItem("🟡 MODERATE", Color(0xFFD97706))
                LegendItem("🟠 CAUTION", Color(0xFFEA580C))
                LegendItem("🔴 UNHEALTHY", Color(0xFFDC2626))
            }
        }
    }
}

@Composable
private fun LegendItem(label: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .clip(CircleShape)
                .background(color)
                .padding(4.dp)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = DeepNavy
        )
    }
}
