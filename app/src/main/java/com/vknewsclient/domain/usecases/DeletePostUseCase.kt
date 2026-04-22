package com.vknewsclient.domain.usecases

import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.repository.NewsFeedRepository

class DeletePostUseCase(
    private val repository: NewsFeedRepository
) {
    suspend operator fun invoke(feedPost: FeedPost) {
        repository.deletePost(feedPost)
    }
}
