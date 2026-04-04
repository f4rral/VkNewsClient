package com.vknewsclient.data.model.comments

import com.google.gson.annotations.SerializedName


data class CommentsGetDto(
    @SerializedName("response") val response: CommentsResponseDto
)
