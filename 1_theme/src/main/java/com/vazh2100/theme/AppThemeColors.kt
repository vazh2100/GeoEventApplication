package com.vazh2100.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

@Suppress("MagicNumber")
internal object AppThemeColors {
    val purple80 = Color(0xFFD0BCFF)
    val purpleGrey80 = Color(0xFFCCC2DC)
    val pink80 = Color(0xFFEFB8C8)
    val purple40 = Color(0xFF6650a4)
    val purpleGrey40 = Color(0xFF625b71)
    val pink40 = Color(0xFF7D5260)
}

internal val darkColorScheme = with(AppThemeColors) {
    darkColorScheme(
        primary = purple80,
        secondary = purpleGrey80,
        tertiary = pink80
    )
}

internal val lightColorScheme = with(AppThemeColors) {
    lightColorScheme(
        primary = purple40,
        secondary = purpleGrey40,
        tertiary = pink40

    )
}
