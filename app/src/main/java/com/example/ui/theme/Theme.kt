package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme =
  darkColorScheme(
    primary = OneUiBlueLight,
    onPrimary = OneUiDarkTextPrimary,
    primaryContainer = OneUiBlueContainerDark,
    onPrimaryContainer = OneUiBlueLight,
    secondary = OneUiDarkTextSecondary,
    background = OneUiDarkBackground,
    surface = OneUiDarkSurface,
    surfaceVariant = OneUiDarkCard,
    onSurface = OneUiDarkTextPrimary,
    onSurfaceVariant = OneUiDarkTextSecondary,
    outline = OneUiDarkBorder,
    error = OneUiRed,
    errorContainer = OneUiRedContainer
  )

private val LightColorScheme =
  lightColorScheme(
    primary = OneUiBlue,
    onPrimary = OneUiLightSurface,
    primaryContainer = OneUiBlueContainerLight,
    onPrimaryContainer = OneUiBlue,
    secondary = OneUiLightTextSecondary,
    background = OneUiLightBackground,
    surface = OneUiLightSurface,
    surfaceVariant = OneUiLightCard,
    onSurface = OneUiLightTextPrimary,
    onSurfaceVariant = OneUiLightTextSecondary,
    outline = OneUiLightBorder,
    error = OneUiRed,
    errorContainer = OneUiLightCard
  )

@Composable
fun MyApplicationTheme(
  darkTheme: Boolean = isSystemInDarkTheme(),
  content: @Composable () -> Unit,
) {
  val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
  MaterialTheme(colorScheme = colorScheme, typography = Typography, content = content)
}

