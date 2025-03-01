package com.vazh2100.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.runtime.Composable

@Composable
internal fun appShapes(): Shapes {
    return Shapes(
        extraSmall = RoundedCornerShape(dimens.four),
        small = RoundedCornerShape(dimens.eight),
        medium = RoundedCornerShape(dimens.twelve),
        large = RoundedCornerShape(dimens.sixteen),
        extraLarge = RoundedCornerShape(dimens.twentyFour)
    )
}
