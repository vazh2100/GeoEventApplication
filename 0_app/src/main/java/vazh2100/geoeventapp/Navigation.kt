package vazh2100.geoeventapp

import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import events.entities.Event
import events.screen.event_detail.EventDetailsScreen
import events.screen.event_list.EventListScreen
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Composable
internal fun AppNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "eventList",
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut
                )
            )
        },
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut
                )
            )
        },
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { -it },
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut
                )
            )
        },
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { it },
                animationSpec = tween(
                    durationMillis = 400,
                    easing = EaseInOut
                )
            )
        }
    ) {
        composable("eventList") {
            EventListScreen(onEventTap = { event ->
                navController.navigate(
                    "eventDetails/${
                        Json.encodeToString(
                            event
                        )
                    }"
                )
            })
        }
        composable("eventDetails/{eventJson}") { backStackEntry ->
            val eventJson = backStackEntry.arguments?.getString("eventJson") ?: ""
            val event = Json.decodeFromString<Event>(eventJson)
            EventDetailsScreen(
                event = event,
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}
