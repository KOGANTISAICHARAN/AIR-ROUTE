package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AirRouteLightColorScheme = lightColorScheme(
  primary = EmeraldGreen,
  onPrimary = Color.White,
  primaryContainer = EmeraldGreenLight,
  onPrimaryContainer = EmeraldGreenDark,
  secondary = DeepNavy,
  onSecondary = Color.White,
  secondaryContainer = WeatherSkyBlueLight,
  onSecondaryContainer = WeatherSkyBlue,
  tertiary = CautionYellow,
  onTertiary = Color.White,
  background = BackgroundOffWhite,
  onBackground = TextPrimary,
  surface = CardSurfaceWhite,
  onSurface = TextPrimary,
  surfaceVariant = BackgroundOffWhite,
  onSurfaceVariant = TextSecondary,
  outline = OutlineBorder
)

private val AirRouteDarkColorScheme = darkColorScheme(
  primary = EmeraldGreen,
  onPrimary = Color.White,
  primaryContainer = EmeraldGreenDark,
  onPrimaryContainer = EmeraldGreenLight,
  secondary = WeatherSkyBlueLight,
  onSecondary = DeepNavy,
  background = DeepNavy,
  onBackground = Color.White,
  surface = NavyLight,
  onSurface = Color.White,
  surfaceVariant = DeepNavy,
  onSurfaceVariant = Color(0xFF94A3B8),
  outline = Color(0xFF334155)
)

@Composable
fun AirRouteTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit
) {
  val colorScheme = if (darkTheme) AirRouteDarkColorScheme else AirRouteLightColorScheme

  MaterialTheme(
    colorScheme = colorScheme,
    typography = Typography,
    content = content
  )
}

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  dynamicColor: Boolean = true,
  content: @Composable () -> Unit
) {
  AirRouteTheme(darkTheme = darkTheme, content = content)
}

