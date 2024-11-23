package com.vazh2100.geoeventapp.presentaion

import EventDetailsScreen
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.presentaion.screen.eventList.EventListScreen
import kotlinx.serialization.json.Json

@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(navController = navController, startDestination = "eventList") {
        composable("eventList") {
            EventListScreen(navController)
        }
        composable("eventDetails/{eventJson}") { backStackEntry ->
            val eventJson = backStackEntry.arguments?.getString("eventJson") ?: ""
            val event = Json.decodeFromString<Event>(eventJson)
            EventDetailsScreen(event = event, navController)
        }
    }
}