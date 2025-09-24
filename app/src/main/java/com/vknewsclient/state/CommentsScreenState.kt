package com.vknewsclient.state

import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.PostComment

sealed class CommentsScreenState {
    object Initial: CommentsScreenState()

    data class Comments(
        val feedPost: FeedPost,
        val comments: List<PostComment>
    ): CommentsScreenState()
}