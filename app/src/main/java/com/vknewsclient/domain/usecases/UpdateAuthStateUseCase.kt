package com.vknewsclient.domain.usecases

import com.vknewsclient.domain.repository.AuthRepository

class UpdateAuthStateUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke() {
        repository.updateAuthState()
    }
}