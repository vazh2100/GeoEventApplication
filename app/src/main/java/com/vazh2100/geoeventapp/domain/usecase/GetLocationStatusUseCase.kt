package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.LocationRepository
import com.vazh2100.geoeventapp.domain.entities.GPoint
import com.vazh2100.geoeventapp.domain.entities.LocationStatus
import kotlinx.coroutines.flow.StateFlow

class GetLocationStatusUseCase(locationRepository: LocationRepository) {
    val locationStatus: StateFlow<LocationStatus> = locationRepository.locationStatus
    val userGPoint: StateFlow<GPoint?> = locationRepository.userGPoint
}