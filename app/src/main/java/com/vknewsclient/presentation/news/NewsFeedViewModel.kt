package com.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vknewsclient.data.repository.NewsFeedRepository
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class NewsFeedViewModel: ViewModel() {
    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    private val repository = NewsFeedRepository()

    init {
        _screenState.value = NewsFeedScreenState.Loading
        loadNewsFeed()
    }

    private fun loadNewsFeed() {
        viewModelScope.launch {
            val feedPosts = repository.loadNewsFeed()
            _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)
        }
    }

    fun loadNextNewsFeed() {
        Log.d("loadNextNewsFeed", "loadNextNewsFeed")

        _screenState.value = NewsFeedScreenState.Posts(
            posts = repository.feedPosts,
            nextDataIsLoading = true,
        )

        loadNewsFeed()
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch {
            repository.changeLikeStatus(feedPost)
            _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
        }
    }

    fun remove(feedPost: FeedPost) {
       viewModelScope.launch {
           repository.deletePost(feedPost)
           _screenState.value = NewsFeedScreenState.Posts(posts = repository.feedPosts)
       }
    }
}