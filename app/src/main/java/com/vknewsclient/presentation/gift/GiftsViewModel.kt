package com.vknewsclient.presentation.gift

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vknewsclient.domain.Gift

class GiftsViewModel: ViewModel() {

    private val sourceList = mutableListOf<Gift>().apply {
        repeat(10) { index ->
            add(Gift(id = index))
        }
    }

    private val initialState = GiftScreenState.Gifts(gifts = sourceList)
    private val _screenState = MutableLiveData<GiftScreenState>(initialState)
    val screenState: LiveData<GiftScreenState> = _screenState
}