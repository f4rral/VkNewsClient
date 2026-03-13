package com.vknewsclient.data.model.newsFeed

import com.google.gson.annotations.SerializedName

data class AttachmentDto(
    @SerializedName("photo") val photo: PhotoDto?
)
