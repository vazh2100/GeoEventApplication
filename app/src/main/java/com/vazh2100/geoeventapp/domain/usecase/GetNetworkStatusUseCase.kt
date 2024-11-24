package com.vazh2100.geoeventapp.domain.usecase

import com.vazh2100.geoeventapp.data.repository.NetworkRepository
import com.vazh2100.geoeventapp.domain.entities.NetworkStatus
import kotlinx.coroutines.flow.StateFlow

class GetNetworkStatusUseCase(private val networkRepository: NetworkRepository) {
    val networkStatus: StateFlow<NetworkStatus> = networkRepository.networkStatus

}