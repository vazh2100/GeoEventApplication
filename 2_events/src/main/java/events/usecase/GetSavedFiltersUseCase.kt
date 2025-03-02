package events.usecase

import events.data.storage.EventsPreferencesStorage
import events.entities.EventSearchParams

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
