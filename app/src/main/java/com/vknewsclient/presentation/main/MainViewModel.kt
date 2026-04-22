package com.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vknewsclient.data.repository.AuthRepository
import kotlinx.coroutines.launch

class MainViewModel(): ViewModel() {

    private val repository = AuthRepository()
    val authState = repository.authState

    fun auth() {
        viewModelScope.launch {
            VKID.instance.authorize(
                callback = _vkAuthCallback,
                params = VKIDAuthParams {
                    scopes = setOf("email", "wall", "friends")
                    oAuth = OAuth.VK
                }
            )
        }
    }

    private val _vkAuthCallback = object : VKIDAuthCallback {
        override fun onAuth(accessToken: AccessToken) {
            viewModelScope.launch {
                repository.updateAuthState()
            }
        }

        override fun onFail(fail: VKIDAuthFail) {
            viewModelScope.launch {
                repository.updateAuthState()
            }
        }
    }
}