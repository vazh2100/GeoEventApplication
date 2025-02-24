package com.vazh2100.network.usecase

import com.vazh2100.network.entity.NetworkStatus
import com.vazh2100.network.repository.NetworkStateRepository
import kotlinx.coroutines.flow.StateFlow

interface IGetNetworkStatusUseCase {
    val networkStatus: StateFlow<NetworkStatus>
}

internal class GetNetworkStatusUseCase(networkStateRepository: NetworkStateRepository) : IGetNetworkStatusUseCase {
    override val networkStatus: StateFlow<NetworkStatus> = networkStateRepository.networkStatus
}
