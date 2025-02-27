package com.vazh2100.geolocation.usecase

import com.vazh2100.geolocation.entity.GPoint
import com.vazh2100.geolocation.entity.LocationStatus
import com.vazh2100.geolocation.repository.LocationStateRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow

interface IGetLocationStatusUseCase {
    operator fun invoke(): Flow<LocationStatus>
    val userGPoint: StateFlow<GPoint?>
}

internal class GetLocationStatusUseCase(
    private val locationRepository: LocationStateRepository
) : IGetLocationStatusUseCase {
    override operator fun invoke(): Flow<LocationStatus> = locationRepository.locationStatus
    override val userGPoint = locationRepository.userGPoint
}
