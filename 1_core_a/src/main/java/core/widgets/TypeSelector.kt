package core.widgets

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import core.entities.DisplayNameEnum
import theme.dimens
import theme.styles

@Composable
fun <T : DisplayNameEnum> TypeSelector(
    label: String,
    currentSelection: MutableState<T?>,
    items: List<T?>,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier) {
        Text(text = label, style = styles.titleSmall)
        Spacer(modifier = Modifier.height(dimens.eight))
        Box {
            var expanded by remember { mutableStateOf(false) }
            OutlinedButton(
                onClick = { expanded = true },
                content = {
                    Text(currentSelection.value?.displayName ?: "Choose type")
                    Icon(Icons.Filled.ArrowDropDown, null)
                },
            )
            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false },
            ) {
                items.forEach { type ->
                    DropdownMenuItem(
                        onClick = {
                            currentSelection.value = type
                            expanded = false
                        },
                        text = { Text(type?.displayName ?: "None") },
                    )
                }
            }
        }
    }
}
