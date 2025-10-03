package com.vknewsclient.domain

import com.vknewsclient.R

data class Gift(
    val id: Int = -1,
    val date: String = "14:00",
    val thumbUrl: Int = R.drawable.gift_thumb,
    val message: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
)
