package com.vknewsclient.data.model.likes

import com.google.gson.annotations.SerializedName

data class LikesCountGetDto(
    @SerializedName("response") val response: LikesCountResponseDto
)
