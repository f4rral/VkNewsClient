package com.vknewsclient.data.model.newsFeed

import com.google.gson.annotations.SerializedName

data class PhotoDto(
    @SerializedName("sizes") val sizes: List<PhotoSizeDto>
)
