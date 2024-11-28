package com.vazh2100.core.presentaion.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vazh2100.core.domain.entities.DisplayNameEnum
import kotlin.collections.forEach

@Composable
fun <T : DisplayNameEnum> TypeSelector(
    currentSelection: T?,
    onSelectionChange: (T?) -> Unit,
    items: List<T?>,
    modifier: Modifier
) {
    var expanded by remember { mutableStateOf(false) }

    Column(modifier = modifier) {
        Text("Event Type", style = MaterialTheme.typography.titleSmall)
        Spacer(modifier = Modifier.height(8.dp))
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

