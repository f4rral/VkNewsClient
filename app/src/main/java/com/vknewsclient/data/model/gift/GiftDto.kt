package com.vknewsclient.data.model.gift

import com.google.gson.annotations.SerializedName

data class GiftDto(
    @SerializedName("id") val id: String,
    @SerializedName("thumb_512") val thumb512: String?,
    @SerializedName("thumb_256") val thumb256: String?,
    @SerializedName("thumb_48") val thumb48: String?,
    @SerializedName("thumb_96") val thumb96: String?,
)
