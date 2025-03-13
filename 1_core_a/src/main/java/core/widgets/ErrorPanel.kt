package core.widgets

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import theme.colors
import theme.dimens
import theme.shapes
import theme.styles

@Composable
fun ErrorPanel(text: String, modifier: Modifier = Modifier) {
    val backgroundColor = colors.error
    val textColor = colors.onError
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(backgroundColor, shape = shapes.extraLarge)
                .padding(dimens.twelve)
        ) {
            Text(
                text = text,
                style = styles.bodyMedium,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
