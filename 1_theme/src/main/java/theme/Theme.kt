package theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

val colors
    @Composable get() = MaterialTheme.colorScheme
val shapes
    @Composable get() = MaterialTheme.shapes
val styles
    @Composable get() = MaterialTheme.typography
val dimens
    @Composable get() = LocalDimens.current
