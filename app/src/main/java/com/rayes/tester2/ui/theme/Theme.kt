package com.rayes.tester2.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

enum class AppTheme {
    DEFAULT, OCEAN, FOREST, ROYAL, DARK
}

private val OceanColorScheme = lightColorScheme(
    primary = Color(0xFF0284C7),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFE0F2FE),
    onPrimaryContainer = Color(0xFF0369A1),
    secondary = Color(0xFF0D9488),
    background = Color(0xFFF0F9FF),
    surface = Color.White
)

private val ForestColorScheme = lightColorScheme(
    primary = Color(0xFF16A34A),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFDCFCE7),
    onPrimaryContainer = Color(0xFF15803D),
    secondary = Color(0xFF4D7C0F),
    background = Color(0xFFF0FDF4),
    surface = Color.White
)

private val RoyalColorScheme = lightColorScheme(
    primary = Color(0xFF7C3AED),
    onPrimary = Color.White,
    primaryContainer = Color(0xFFF5F3FF),
    onPrimaryContainer = Color(0xFF6D28D9),
    secondary = Color(0xFFDB2777),
    background = Color(0xFFFAF5FF),
    surface = Color.White
)

private val DarkColorScheme = darkColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = Slate800,
    onPrimaryContainer = OnPrimary,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = Slate700,
    onSecondaryContainer = OnSecondary,
    tertiary = Tertiary,
    background = Slate900,
    surface = Slate800,
    onBackground = Color.White,
    onSurface = Color.White,
    error = Error
)

private val LightColorScheme = lightColorScheme(
    primary = Primary,
    onPrimary = OnPrimary,
    primaryContainer = PrimaryContainer,
    onPrimaryContainer = OnPrimaryContainer,
    secondary = Secondary,
    onSecondary = OnSecondary,
    secondaryContainer = SecondaryContainer,
    onSecondaryContainer = OnSecondaryContainer,
    tertiary = Tertiary,
    onTertiary = OnTertiary,
    tertiaryContainer = TertiaryContainer,
    onTertiaryContainer = OnTertiaryContainer,
    background = Background,
    surface = Surface,
    onBackground = Slate900,
    onSurface = Slate900,
    error = Error
)

val LocalAppTheme = compositionLocalOf { mutableStateOf(AppTheme.DEFAULT) }

@Composable
fun Tester2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val currentThemeState = LocalAppTheme.current
    
    val colorScheme = when (currentThemeState.value) {
        AppTheme.OCEAN -> OceanColorScheme
        AppTheme.FOREST -> ForestColorScheme
        AppTheme.ROYAL -> RoyalColorScheme
        AppTheme.DARK -> DarkColorScheme
        AppTheme.DEFAULT -> {
            if (darkTheme) DarkColorScheme else LightColorScheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
