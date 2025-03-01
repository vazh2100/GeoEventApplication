package com.vazh2100.theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.platform.LocalConfiguration

@Composable
internal fun ScaleProvider(content: @Composable () -> Unit) {
    val config = LocalConfiguration.current
    val screenWidth = config.screenWidthDp
    val screenHeight = config.screenHeightDp
    val scale = when {
        screenWidth <= SMALL_WIDTH || screenHeight <= SMALL_HEIGHT -> SMALL_SCALE
        screenWidth <= MEDIUM_WIDTH || screenHeight <= MEDIUM_HEIGHT -> MEDIUM_SCALE
        else -> LARGE_SCALE
    }
    CompositionLocalProvider(LocalScale provides scale) {
        content()
    }
}

@Suppress("CompositionLocalAllowlist")
internal val LocalScale = staticCompositionLocalOf { 1f }
private const val SMALL_WIDTH = 360
private const val MEDIUM_WIDTH = 600
private const val SMALL_HEIGHT = 480
private const val MEDIUM_HEIGHT = 800
private const val SMALL_SCALE = 0.8f
private const val MEDIUM_SCALE = 1f
private const val LARGE_SCALE = 1.2f
