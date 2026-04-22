package com.vknewsclient.data.repository

import android.util.Log
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import com.vknewsclient.domain.entity.AuthState
import com.vknewsclient.domain.repository.AuthRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.util.Date


class AuthRepositoryImpl: AuthRepository {
    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val authStateEvent = MutableSharedFlow<Unit>(replay = 1)

    private val authStateFlow = flow {
        authStateEvent.emit(Unit)

        authStateEvent.collect {
            val accessToken = VKID.instance.accessToken
            val expireTime = accessToken?.expireTime ?: 0
            val nowDate = Date()

            Log.d("MainViewModel", "expireTime: $expireTime")
            Log.d("MainViewModel", "nowDate:    ${nowDate.time}")
            Log.d("MainViewModel", "if:         ${nowDate.time > expireTime}")

            if (accessToken == null) {
                emit(AuthState.NotAuthorized)
                return@collect
            }

            if (nowDate.time > expireTime ) {
                refreshToken()
                return@collect
            }

            emit(AuthState.Authorized)
        }
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = AuthState.Initial
    )

    override suspend fun updateAuthState() {
        authStateEvent.emit(Unit)
    }

    override fun getAuthStateFlow(): StateFlow<AuthState> {
        return authStateFlow
    }

    private fun refreshToken() {
        coroutineScope.launch {
            VKID.instance.refreshToken(
                callback = refreshCallback
            )
        }
    }

    private val refreshCallback = object : VKIDRefreshTokenCallback {
        override fun onSuccess(token: AccessToken) {
            coroutineScope.launch {
                Log.d("MainViewModel", "refreshToken onSuccess ${token.token}")

                authStateEvent.emit(Unit)
                logToken()
            }
        }
        override fun onFail(fail: VKIDRefreshTokenFail) {
            coroutineScope.launch {
                Log.d("MainViewModel", "refreshToken onFail ${fail.description}")

                authStateEvent.emit(Unit)
                logToken()
            }
        }
    }

    private fun logToken() {
        Log.d("MainViewModel", "accessToken token ${VKID.instance.accessToken?.token}")
        Log.d("MainViewModel", "accessToken idToken ${VKID.instance.accessToken?.idToken}")
        Log.d("MainViewModel", "accessToken userID ${VKID.instance.accessToken?.userID}")
        Log.d("MainViewModel", "accessToken expireTime ${VKID.instance.accessToken?.expireTime}")
        Log.d("MainViewModel", "accessToken scopes ${VKID.instance.accessToken?.scopes}")
        Log.d("MainViewModel", "accessToken userData ${VKID.instance.accessToken?.userData}")
    }
}