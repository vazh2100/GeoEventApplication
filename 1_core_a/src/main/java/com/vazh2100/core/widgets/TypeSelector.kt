package com.vazh2100.core.widgets

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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.vazh2100.core.entities.DisplayNameEnum
import com.vazh2100.theme.dimens
import com.vazh2100.theme.styles

@Composable
fun <T : DisplayNameEnum> TypeSelector(
    label: String,
    currentSelection: T?,
    onSelectionChange: (T?) -> Unit,
    items: List<T?>,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text(label, style = styles.titleSmall)
        Spacer(modifier = Modifier.height(dimens.eight))
        Box {
            OutlinedButton(onClick = { expanded = true }) {
                Text(currentSelection?.displayName ?: "Choose type")
                Icon(Icons.Filled.ArrowDropDown, contentDescription = null)
            }
            DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                items.forEach { type ->
                    DropdownMenuItem(onClick = {
                        onSelectionChange(type)
                        expanded = false
                    }, text = { Text(type?.displayName ?: "None") })
                }
            }
        }
    }
}
