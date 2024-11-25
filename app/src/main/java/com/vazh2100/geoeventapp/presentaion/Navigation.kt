package com.vazh2100.geoeventapp.presentaion

import EventDetailsScreen
import androidx.compose.animation.*
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.presentaion.screen.eventList.EventListScreen
import kotlinx.serialization.json.Json

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "eventList",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it }, // Вход справа
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut // Сглаживание движения
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it }, // Уход влево
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut // Сглаживание движения
                )
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it }, // Возврат с левой стороны
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut // Сглаживание движения
                )
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it }, // Уход вправо
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut // Сглаживание движения
                )
            )
        }
    ) {
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

