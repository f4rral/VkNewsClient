package com.vknewsclient.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.vknewsclient.ui.theme.Black900

private val DarkColorScheme = darkColorScheme(
    primary = Black900,
    secondary = Black900,
    onPrimary = Color.White,
    onSecondary = Black500,
    surfaceContainerHighest = Black900,
)

private val LightColorScheme = lightColorScheme(
    primary = Color.White,
    secondary = Color.White,
    onPrimary = Black900,
    onSecondary = Black500,
    surfaceContainerHighest = Color.White,
)

@Composable
fun VkNewsClientTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}