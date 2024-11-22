package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.EventRepository
import com.vazh2100.geoeventapp.domain.entities.Event
import com.vazh2100.geoeventapp.domain.entities.EventType
import java.time.ZonedDateTime

class GetFilteredEventsUseCase(
    private val eventRepository: EventRepository
) {
    suspend fun execute(
        type: EventType? = null,
        startDate: ZonedDateTime? = null,
        endDate: ZonedDateTime? = null,
        userLatitude: Double? = null,
        userLongitude: Double? = null,
        radius: Double? = null
    ): Result<List<Event>> {
        return try {
            // Получаем отфильтрованные события из репозитория
            val filteredEvents = eventRepository.getFilteredEvents(
                type = type,
                startDate = startDate,
                endDate = endDate,
                userLatitude = userLatitude,
                userLongitude = userLongitude,
                radius = radius
            )

            // Если список пустой, возвращаем пустой результат
            if (filteredEvents.isEmpty()) {
                Result.success(emptyList())
            } else {
                Result.success(filteredEvents)
            }
        } catch (e: Exception) {
            // В случае ошибки возвращаем ошибочное состояние
            Result.failure(e)
        }
    }
}
