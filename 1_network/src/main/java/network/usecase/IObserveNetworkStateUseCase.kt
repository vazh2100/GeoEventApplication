package network.usecase

import kotlinx.coroutines.flow.StateFlow
import network.entity.NetworkStatus
import network.repository.NetworkStateRepository

interface IObserveNetworkStateUseCase {
    operator fun invoke(): StateFlow<NetworkStatus>
}

internal class ObserveNetworkStateUseCase(
    private val networkStateRepository: NetworkStateRepository
) : IObserveNetworkStateUseCase {

    override operator fun invoke() = networkStateRepository.networkState
}
