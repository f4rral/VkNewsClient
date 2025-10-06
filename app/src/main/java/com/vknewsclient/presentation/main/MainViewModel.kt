package com.vknewsclient.presentation.main

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vk.id.refresh.VKIDRefreshTokenCallback
import com.vk.id.refresh.VKIDRefreshTokenFail
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        val accessToken = VKID.instance.accessToken

        if (accessToken == null) {
            _authState.value = AuthState.NotAuthorized
        } else {
            _authState.value = AuthState.Authorized
            refreshToken()
        }
    }

    fun auth() {
        viewModelScope.launch {
            VKID.instance.authorize(
                callback = _vkAuthCallback,
                params = VKIDAuthParams {
                    scopes = setOf("email", "wall")
                    oAuth = OAuth.VK
                }
            )
        }
    }

    fun refreshToken() {
        viewModelScope.launch {
            viewModelScope.launch {
                VKID.instance.refreshToken(
                    callback = object : VKIDRefreshTokenCallback {
                        override fun onSuccess(token: AccessToken) {
                            Log.d("MainViewModel", "refreshToken onSuccess")
                            logToken()
                        }
                        override fun onFail(fail: VKIDRefreshTokenFail) {
                            Log.d("MainViewModel", "refreshToken onFail ${fail.description}")
                        }
                    }
                )
            }
        }
    }

    fun logToken() {
        Log.d("MainViewModel", "accessToken token ${VKID.instance.accessToken?.token}")
        Log.d("MainViewModel", "accessToken idToken ${VKID.instance.accessToken?.idToken}")
        Log.d("MainViewModel", "accessToken userID ${VKID.instance.accessToken?.userID}")
        Log.d("MainViewModel", "accessToken expireTime ${VKID.instance.accessToken?.expireTime}")
        Log.d("MainViewModel", "accessToken scopes ${VKID.instance.accessToken?.scopes}")
        Log.d("MainViewModel", "accessToken userData ${VKID.instance.accessToken?.userData}")
    }

    private val _vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            _authState.value = AuthState.Authorized
        }

        override fun onFail(fail: VKIDAuthFail) {
            _authState.value = AuthState.NotAuthorized
        }
    }
}