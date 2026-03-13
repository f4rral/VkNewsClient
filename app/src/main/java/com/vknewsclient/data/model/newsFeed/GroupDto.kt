package com.vknewsclient.data.model.newsFeed

import com.google.gson.annotations.SerializedName

data class GroupDto(
    @SerializedName("id") var id: Long,
    @SerializedName("name") var name: String,
    @SerializedName("photo_200") var photoUrl200: String,
)
