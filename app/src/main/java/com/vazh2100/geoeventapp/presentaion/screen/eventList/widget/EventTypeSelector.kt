package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.foundation.layout.Box
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
import com.vazh2100.geoeventapp.domain.entities.EventType
import kotlin.collections.forEach

@Composable
fun EventTypeSelector(
    currentSelection: EventType?, onSelectionChange: (EventType?) -> Unit, items: List<EventType?>
) {
    var expanded by remember { mutableStateOf(false) }
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