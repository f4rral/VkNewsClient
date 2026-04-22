package com.vknewsclient.domain.usecases

import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.repository.NewsFeedRepository
import kotlinx.coroutines.flow.StateFlow

class GetNewsFeedUseCase(
    private val repository: NewsFeedRepository
) {
    operator fun invoke(): StateFlow<List<FeedPost>> {
        return repository.getNewsFeed()
    }
}