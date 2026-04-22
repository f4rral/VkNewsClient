package com.vknewsclient.presentation.comments

import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.entity.PostComment

sealed class CommentsScreenState {
    object Initial: CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ): CommentsScreenState()
}