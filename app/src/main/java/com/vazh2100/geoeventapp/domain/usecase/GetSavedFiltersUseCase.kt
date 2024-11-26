package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.storages.device.PreferencesStorage
import com.vazh2100.geoeventapp.domain.entities.event.EventSearchParams


class GetSavedFiltersUseCase(
    private val preferencesStorage: PreferencesStorage
) {

    suspend fun get(): Result<EventSearchParams> {
        return try {
            val filter = preferencesStorage.getEventSearchParams()
            Result.success(filter)
        } catch (_: Exception) {
            Result.failure(Exception("Unable to load saved filters"))
        }
    }
}

