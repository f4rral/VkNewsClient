package com.vknewsclient.presentation.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.OAuth
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vk.id.auth.VKIDAuthParams
import com.vknewsclient.data.repository.AuthRepositoryImpl
import com.vknewsclient.domain.usecases.GetAuthStateFlowUseCase
import com.vknewsclient.domain.usecases.UpdateAuthStateUseCase
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val repository = AuthRepositoryImpl()

    private val getAuthStateFlowUseCase = GetAuthStateFlowUseCase(repository)
    private val updateAuthStateUseCase = UpdateAuthStateUseCase(repository)

    val authState = getAuthStateFlowUseCase()

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
                updateAuthStateUseCase()
            }
        }

        override fun onFail(fail: VKIDAuthFail) {
            viewModelScope.launch {
                updateAuthStateUseCase()
            }
        }
    }
}