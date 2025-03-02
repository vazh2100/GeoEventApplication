package theme

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.runtime.staticCompositionLocalOf

@Composable
internal fun DimensionsProvider(content: @Composable () -> Unit) {
    val scale = LocalScale.current
    val dimens = remember(scale) { Dimensions(scale) }
    CompositionLocalProvider(LocalDimens provides dimens) {
        content()
    }
}

@Suppress("CompositionLocalAllowlist")
internal val LocalDimens = staticCompositionLocalOf<Dimensions> {
    error("Not provided")
}
