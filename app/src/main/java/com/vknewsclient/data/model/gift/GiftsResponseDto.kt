package com.vknewsclient.data.model.gift

import com.google.gson.annotations.SerializedName

data class GiftsResponseDto(
    @SerializedName("response") val giftsContent: GiftsContentDto
)
