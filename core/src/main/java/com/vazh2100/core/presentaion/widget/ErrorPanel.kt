package com.vazh2100.core.presentaion.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun ErrorPanel(text: String, modifier: Modifier = Modifier) {
    val backgroundColor = MaterialTheme.colorScheme.error
    val textColor = MaterialTheme.colorScheme.onError
    Box(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    backgroundColor, shape = RoundedCornerShape(24.dp)
                )
                .padding(10.dp)
        ) {
            Text(
                text = text,
                style = MaterialTheme.typography.bodyMedium,
                color = textColor,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}