package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.HourlyForecast
import com.example.ui.theme.CardSurfaceWhite
import com.example.ui.theme.DeepNavy
import com.example.ui.theme.EmeraldGreen
import com.example.ui.theme.TextSecondary

@Composable
fun ForecastChart(
    hourlyForecast: List<HourlyForecast>,
    modifier: Modifier = Modifier
) {
    var selectedPointIndex by remember(hourlyForecast) { mutableStateOf(0) }
    val progress = remember { Animatable(0f) }

    LaunchedEffect(hourlyForecast) {
        progress.snapTo(0f)
        progress.animateTo(1f, animationSpec = tween(800))
    }

    val selectedPoint = hourlyForecast.getOrNull(selectedPointIndex) ?: hourlyForecast.firstOrNull()

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("forecast_chart_card"),
        shape = RoundedCornerShape(32.dp),
        colors = CardDefaults.cardColors(containerColor = CardSurfaceWhite),
        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFF1F5F9)),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp)
        ) {
            // Selected point details header
            selectedPoint?.let { point ->
                val badgeColor = Color(point.recommendationState.hexColor)
                val badgeBg = Color(point.recommendationState.bgHexColor)

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "${point.timeLabel} ${if (point.isPredicted) "(Predicted)" else "(Current)"}",
                            fontSize = 12.sp,
                            color = TextSecondary,
                            fontWeight = FontWeight.Medium
                        )
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${point.pm25}",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = DeepNavy
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "µg/m³ PM2.5",
                                fontSize = 13.sp,
                                color = TextSecondary
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(badgeBg)
                            .padding(horizontal = 14.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = point.statusLabel,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = badgeColor
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Canvas Chart Area
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .pointerInput(hourlyForecast) {
                            detectTapGestures { offset ->
                                val width = size.width
                                val step = width / (hourlyForecast.size - 1).coerceAtLeast(1)
                                val index = (offset.x / step).toInt().coerceIn(0, hourlyForecast.size - 1)
                                selectedPointIndex = index
                            }
                        }
                ) {
                    if (hourlyForecast.isEmpty()) return@Canvas

                    val maxPm = (hourlyForecast.maxOfOrNull { it.pm25 } ?: 100).coerceAtLeast(120).toFloat()
                    val minPm = 0f
                    val chartWidth = size.width
                    val chartHeight = size.height - 40f // padding at bottom for labels

                    val points = hourlyForecast.mapIndexed { i, item ->
                        val x = if (hourlyForecast.size == 1) chartWidth / 2 else i * (chartWidth / (hourlyForecast.size - 1))
                        val y = chartHeight - ((item.pm25 - minPm) / (maxPm - minPm)) * (chartHeight - 30f) - 15f
                        Offset(x, y)
                    }

                    // Draw Threshold reference lines
                    val yThresholdGood = chartHeight - ((50f - minPm) / (maxPm - minPm)) * (chartHeight - 30f) - 15f
                    drawLine(
                        color = Color(0xFF10B981).copy(alpha = 0.2f),
                        start = Offset(0f, yThresholdGood),
                        end = Offset(chartWidth, yThresholdGood),
                        strokeWidth = 2f
                    )

                    val yThresholdCaution = chartHeight - ((100f - minPm) / (maxPm - minPm)) * (chartHeight - 30f) - 15f
                    if (yThresholdCaution > 0) {
                        drawLine(
                            color = Color(0xFFF59E0B).copy(alpha = 0.2f),
                            start = Offset(0f, yThresholdCaution),
                            end = Offset(chartWidth, yThresholdCaution),
                            strokeWidth = 2f
                        )
                    }

                    // Path calculation with animated progress
                    val linePath = Path()
                    val fillPath = Path()

                    points.forEachIndexed { i, pt ->
                        val animatedY = chartHeight - (chartHeight - pt.y) * progress.value
                        if (i == 0) {
                            linePath.moveTo(pt.x, animatedY)
                            fillPath.moveTo(pt.x, chartHeight)
                            fillPath.lineTo(pt.x, animatedY)
                        } else {
                            val prevPt = points[i - 1]
                            val prevAnimatedY = chartHeight - (chartHeight - prevPt.y) * progress.value
                            val controlX1 = prevPt.x + (pt.x - prevPt.x) / 2
                            val controlX2 = prevPt.x + (pt.x - prevPt.x) / 2
                            linePath.cubicTo(controlX1, prevAnimatedY, controlX2, animatedY, pt.x, animatedY)
                            fillPath.cubicTo(controlX1, prevAnimatedY, controlX2, animatedY, pt.x, animatedY)
                        }
                    }

                    fillPath.lineTo(points.last().x, chartHeight)
                    fillPath.close()

                    // Draw Gradient Fill under line
                    drawPath(
                        path = fillPath,
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF059669).copy(alpha = 0.25f),
                                Color(0xFF059669).copy(alpha = 0.02f)
                            )
                        )
                    )

                    // Draw Line
                    drawPath(
                        path = linePath,
                        color = Color(0xFF059669),
                        style = Stroke(width = 4.dp.toPx(), cap = StrokeCap.Round)
                    )

                    // Draw Points
                    points.forEachIndexed { i, pt ->
                        val animatedY = chartHeight - (chartHeight - pt.y) * progress.value
                        val item = hourlyForecast[i]
                        val isSelected = i == selectedPointIndex
                        val ptColor = Color(item.recommendationState.hexColor)

                        // Outer ring if selected
                        if (isSelected) {
                            drawCircle(
                                color = ptColor.copy(alpha = 0.3f),
                                radius = 12.dp.toPx(),
                                center = Offset(pt.x, animatedY)
                            )
                        }

                        drawCircle(
                            color = if (isSelected) ptColor else Color.White,
                            radius = if (isSelected) 7.dp.toPx() else 4.dp.toPx(),
                            center = Offset(pt.x, animatedY)
                        )
                        drawCircle(
                            color = ptColor,
                            radius = if (isSelected) 5.dp.toPx() else 3.dp.toPx(),
                            center = Offset(pt.x, animatedY)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Time Labels row underneath
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                hourlyForecast.forEachIndexed { i, forecast ->
                    Text(
                        text = forecast.timeLabel,
                        fontSize = 11.sp,
                        fontWeight = if (i == selectedPointIndex) FontWeight.Bold else FontWeight.Normal,
                        color = if (i == selectedPointIndex) DeepNavy else TextSecondary
                    )
                }
            }
        }
    }
}
