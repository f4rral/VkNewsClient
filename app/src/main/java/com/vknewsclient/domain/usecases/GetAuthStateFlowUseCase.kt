package com.vknewsclient.domain.usecases

import com.vknewsclient.domain.entity.AuthState
import com.vknewsclient.domain.repository.AuthRepository
import kotlinx.coroutines.flow.StateFlow

class GetAuthStateFlowUseCase(
    private val repository: AuthRepository
) {
    operator fun invoke(): StateFlow<AuthState> {
        return repository.getAuthStateFlow()
    }
}