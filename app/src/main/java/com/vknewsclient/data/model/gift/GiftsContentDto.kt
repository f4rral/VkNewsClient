package com.vknewsclient.data.model.gift

import com.google.gson.annotations.SerializedName

data class GiftsContentDto(
    @SerializedName("count") val count: Long,
    @SerializedName("items") val items: List<GiftUserDto>,
)
