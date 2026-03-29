package com.vknewsclient.data.model.likes

import com.google.gson.annotations.SerializedName

data class LikesCountResponseDto(
    @SerializedName("likes") val likes: Int
)
