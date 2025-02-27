package com.vazh2100.feature_events.domain.usecase

import com.vazh2100.feature_events.data.storages.device.EventsPreferencesStorage
import com.vazh2100.feature_events.domain.entities.event.EventSearchParams

internal class GetSavedFiltersUseCase(
    private val eventsPreferencesStorage: EventsPreferencesStorage
) {

    suspend operator fun invoke(): Result<EventSearchParams> {
        return try {
            val filter = eventsPreferencesStorage.getEventSearchParams()
            Result.success(filter)
        } catch (_: Exception) {
            Result.failure(Exception("Unable to load saved filters"))
        }
    }
}
