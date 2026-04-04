package com.vknewsclient.data.model.comments

import com.google.gson.annotations.SerializedName


data class CommentsResponseDto(
    @SerializedName("count") val count: Int,
    @SerializedName("items") val comments: List<CommentDto>,
    @SerializedName("profiles") val profiles: List<ProfileDto>,
)
