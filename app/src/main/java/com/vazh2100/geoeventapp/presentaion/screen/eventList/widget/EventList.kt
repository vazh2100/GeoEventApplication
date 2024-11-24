package com.vazh2100.geoeventapp.presentaion.screen.eventList.widget

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.vazh2100.geoeventapp.domain.entities.Event
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
fun EventList(
    events: List<Event>,
    userCoordinates: Pair<Double, Double>?,
    navController: NavController
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        items(events) { event ->
            EventListItem(
                event = event,
                onClick = {
                    navController.navigate("eventDetails/${Json.encodeToString(event)}")
                },
                userCoordinates = userCoordinates
            )
        }
    }
}

@Composable
fun LoadingIndicator() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CircularProgressIndicator()
    }
}

@Composable
fun ErrorMessage(errorMessage: String?) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = errorMessage ?: "An error occurred")
    }
}

@Composable
fun EmptyMessage() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Text(text = "No events available")
    }
}