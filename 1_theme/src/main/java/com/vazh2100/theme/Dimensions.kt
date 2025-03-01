package com.vazh2100.theme

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class Dimensions(
    private val scale: Float,
    val zero: Dp = 0.dp,
    val one: Dp = 1.dp * scale,
    val four: Dp = 4.dp * scale,
    val eight: Dp = 8.dp * scale,
    val twelve: Dp = 12.dp * scale,
    val sixteen: Dp = 16.dp * scale,
    val twentyFour: Dp = 24.dp * scale,
    val thirtySix: Dp = 36.dp * scale,
)
