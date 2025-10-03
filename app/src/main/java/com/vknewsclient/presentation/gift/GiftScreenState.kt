package com.vknewsclient.presentation.gift

import com.vknewsclient.domain.Gift

sealed class GiftScreenState {

    object Initial: GiftScreenState()

    data class Gifts(val gifts: List<Gift>): GiftScreenState()
}