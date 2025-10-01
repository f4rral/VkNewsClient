package com.vknewsclient.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vknewsclient.state.AuthState
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val _authState = MutableLiveData<AuthState>(AuthState.Initial)
    val authState: LiveData<AuthState> = _authState

    init {
        _authState.value = if (VKID.instance.accessToken?.idToken.isNullOrEmpty()) AuthState.NotAuthorized else AuthState.Authorized
    }

    fun auth() {
        viewModelScope.launch {
            VKID.instance.authorize(_vkAuthCallback)
        }
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