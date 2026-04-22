package com.vknewsclient.domain.repository

import com.vknewsclient.domain.entity.AuthState
import kotlinx.coroutines.flow.StateFlow


interface AuthRepository {

    fun getAuthStateFlow(): StateFlow<AuthState>

    suspend fun updateAuthState()
}