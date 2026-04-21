package com.vknewsclient.presentation.news

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vknewsclient.data.repository.NewsFeedRepository
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel: ViewModel() {

    private val repository = NewsFeedRepository()

    private val newsFeedFlow = repository.newsFeedFlow
    private val newsFeedScreenStateFlow = MutableSharedFlow<NewsFeedScreenState>()

    val screenState = newsFeedFlow
        .filter {
            it.isNotEmpty()
        }
        .map {
            NewsFeedScreenState.Posts(posts = it) as NewsFeedScreenState
        }
        .onStart {
            emit(NewsFeedScreenState.Loading)
        }
        .mergeWith(newsFeedScreenStateFlow)

    fun loadNextNewsFeed() {
        viewModelScope.launch {
            newsFeedScreenStateFlow.emit(
                NewsFeedScreenState.Posts(
                    posts = newsFeedFlow.value,
                    nextDataIsLoading = true,
                )
            )
            repository.loadNextData()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
       viewModelScope.launch {
           repository.deletePost(feedPost)
       }
    }
}