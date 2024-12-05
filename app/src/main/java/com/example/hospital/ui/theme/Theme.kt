package com.example.hospital.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.lightColorScheme
import androidx.compose.material3.darkColorScheme


// Light color scheme
val LightColorPalette = lightColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = BackgroundColor,
    surface = SurfaceColor,
    onPrimary = Color.White,
    onSecondary = TextPrimary,
    onBackground = TextPrimary,
    onSurface = TextSecondary,
    error = ErrorColor,
    onError = OnErrorColor
)

// Dark color scheme
val DarkColorPalette = darkColorScheme(
    primary = PrimaryColor,
    secondary = SecondaryColor,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color.White,
    onSecondary = Color.LightGray,
    onBackground = Color.White,
    onSurface = Color.LightGray,
    error = ErrorColor,
    onError = OnErrorColor
)

@Composable
fun HospitalTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colorScheme = colors,
        typography = Typography,
        //shapes = Shapes,
        content = content
    )
}
