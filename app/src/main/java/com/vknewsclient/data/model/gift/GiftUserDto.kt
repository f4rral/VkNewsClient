package com.vknewsclient.data.model.gift

import com.google.gson.annotations.SerializedName

data class GiftUserDto(
    @SerializedName("id") val id: String,
    @SerializedName("date") val date: Long,
    @SerializedName("from_id") val fromId: Int,
    @SerializedName("privacy") val privacy: Int,
    @SerializedName("message") val message: String,
    @SerializedName("gift") val gift: GiftDto,
)
