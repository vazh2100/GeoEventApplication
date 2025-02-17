package com.vazh2100.core.domain.usecase

import com.vazh2100.core.data.repository.LocationRepository
import com.vazh2100.core.domain.entities.GPoint
import com.vazh2100.core.domain.entities.LocationStatus
import kotlinx.coroutines.flow.StateFlow

interface IGetLocationStatusUseCase {

    val locationStatus: StateFlow<LocationStatus>
    val userGPoint: StateFlow<GPoint?>
}

internal class GetLocationStatusUseCase(locationRepository: LocationRepository) :
    IGetLocationStatusUseCase {

    override val locationStatus: StateFlow<LocationStatus> = locationRepository.locationStatus
    override val userGPoint: StateFlow<GPoint?> = locationRepository.userGPoint
}
