package com.vknewsclient.state

import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.PostComment

sealed class NewsFeedScreenState {

    object Initial: NewsFeedScreenState()

    data class Posts(val posts: List<FeedPost>): NewsFeedScreenState()
}