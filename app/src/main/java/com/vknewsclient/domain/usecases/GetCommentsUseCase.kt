package com.vknewsclient.domain.usecases

import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.entity.PostComment
import com.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetCommentsUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(feedPost: FeedPost): StateFlow<List<PostComment>> {
        return repository.getComments(feedPost)
    }
}