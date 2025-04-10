package geolocation.usecase

import geolocation.entity.GPoint
import geolocation.entity.LocationStatus
import geolocation.repository.LocationStateRepository
import kotlinx.coroutines.flow.StateFlow

interface IGetLocationStatusUseCase {
    operator fun invoke(): StateFlow<LocationStatus>
    val userGPoint: StateFlow<GPoint?>
}

internal class GetLocationStatusUseCase(
    private val locationRepository: LocationStateRepository
) : IGetLocationStatusUseCase {
    override operator fun invoke(): StateFlow<LocationStatus> = locationRepository.locationStatus
    override val userGPoint = locationRepository.userGPoint
}
