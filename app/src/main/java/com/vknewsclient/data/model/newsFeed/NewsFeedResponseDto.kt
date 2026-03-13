package com.vknewsclient.data.model.newsFeed

import com.google.gson.annotations.SerializedName

data class NewsFeedResponseDto(
    @SerializedName("items") var items: List<PostDto>,
    @SerializedName("groups") var groups: List<GroupDto>,
)
