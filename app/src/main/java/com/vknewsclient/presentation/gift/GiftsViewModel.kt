package com.vknewsclient.presentation.gift

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.VKID
import com.vknewsclient.data.mapper.GiftMapper
import com.vknewsclient.data.network.ApiFactory
import kotlinx.coroutines.launch

class GiftsViewModel: ViewModel() {
    private val initialState = GiftScreenState.Initial
    private val _screenState = MutableLiveData<GiftScreenState>(initialState)
    val screenState: LiveData<GiftScreenState> = _screenState
    private val mapper = GiftMapper()

    init {
        loadGifts()
    }

    private fun loadGifts() {
        viewModelScope.launch {
            val accessToken = VKID.instance.accessToken

            if (accessToken == null) {
                return@launch
            }

            Log.d("GiftsViewModel", "token ${accessToken.token}")

            val response = ApiFactory.apiService.loadGifts(token = accessToken.token)

            val gifts = mapper.mapResponseToGifts(response)
            _screenState.value = GiftScreenState.Gifts(gifts = gifts)
        }
    }
}