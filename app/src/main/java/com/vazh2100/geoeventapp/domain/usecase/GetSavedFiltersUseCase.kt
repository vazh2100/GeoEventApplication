package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.domain.entities.EventFilter


class GetSavedFiltersUseCase(
    private val preferencesStorage: PreferencesStorage
) {
    suspend fun execute(): Result<EventFilter> {
        return try {
            val filter = preferencesStorage.getEventFilter()
            Result.success(filter)
        } catch (_: Exception) {
            Result.failure(Exception("Unable to load saved filters"))
        }
    }
}

