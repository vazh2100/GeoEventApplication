package com.vazh2100.core.domain.usecase

import com.vazh2100.core.data.repository.NetworkStateRepository
import com.vazh2100.core.domain.entities.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

interface IGetNetworkStatusUseCase {

    val networkStatus: StateFlow<NetworkStatus>
}

internal class GetNetworkStatusUseCase(networkStateRepository: NetworkStateRepository) :
    IGetNetworkStatusUseCase {

    override val networkStatus: StateFlow<NetworkStatus> = networkStateRepository.networkStatus
}
