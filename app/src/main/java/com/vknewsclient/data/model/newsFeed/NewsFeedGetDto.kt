package com.vknewsclient.data.model.newsFeed

import com.google.gson.annotations.SerializedName

data class NewsFeedGetDto(
    @SerializedName("response") val response: NewsFeedResponseDto
)
