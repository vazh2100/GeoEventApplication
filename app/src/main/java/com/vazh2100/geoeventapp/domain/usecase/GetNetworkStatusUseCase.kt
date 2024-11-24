package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.NetworkStateRepository
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

class GetNetworkStatusUseCase(private val networkStateRepository: NetworkStateRepository) {
    val networkStatus: StateFlow<NetworkStatus> = networkStateRepository.networkStatus

}