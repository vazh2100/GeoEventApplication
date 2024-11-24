package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.LocationRepository
import com.vazh2100.geoeventapp.domain.entities.LocationStatus
import kotlinx.coroutines.flow.StateFlow

class GetLocationStatusUseCase(private val locationRepository: LocationRepository) {
    val locationStatus: StateFlow<LocationStatus> = locationRepository.locationStatus
    val currentCoordinates: StateFlow<Pair<Double, Double>?> = locationRepository.currentCoordinates
}