package com.vknewsclient.presentation.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vk.id.VKID
import com.vknewsclient.data.mapper.NewsFeedMapper
import com.vknewsclient.data.network.ApiFactory
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem
import kotlinx.coroutines.launch

class NewsFeedViewModel: ViewModel() {
    private val initialState = NewsFeedScreenState.Initial

    private val _screenState = MutableLiveData<NewsFeedScreenState>(initialState)
    val screenState: LiveData<NewsFeedScreenState> = _screenState

    val mapper = NewsFeedMapper()

    init {
        loadNewsFeed()
    }

    private fun loadNewsFeed() {
        viewModelScope.launch {
            val accessToken = VKID.instance.accessToken

            if (accessToken == null) {
                return@launch
            }

            val response = ApiFactory.apiService.loadNewsFeed(token = accessToken.token)

            val feedPosts = mapper.mapResponseToPostDto(response)
            
            _screenState.value = NewsFeedScreenState.Posts(posts = feedPosts)
        }
    }

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val currentState = screenState.value

        if (currentState !is NewsFeedScreenState.Posts) {
            return
        }

        val oldPosts = currentState.posts.toMutableList()
        val oldStatistics = feedPost.statistics

        val newStatistics = oldStatistics.toMutableList().apply {
            replaceAll { oldItem ->
                if (oldItem.type === statisticItem.type) {
                    oldItem.copy(count = oldItem.count + 1)
                } else {
                    oldItem
                }
            }
        }

        val newFeedPost = feedPost.copy(statistics = newStatistics)

        val newPosts = oldPosts.apply {
            replaceAll {
                if (it.id == newFeedPost.id) {
                    newFeedPost
                } else {
                    it
                }
            }
        }

        _screenState.value = NewsFeedScreenState.Posts(posts = newPosts)
    }

    fun remove(feedPost: FeedPost) {
        val currentState = screenState.value

        if (currentState !is NewsFeedScreenState.Posts) {
            return
        }

        val oldPosts = currentState.posts.toMutableList()
        oldPosts.remove(feedPost)

        _screenState.value = NewsFeedScreenState.Posts(posts = oldPosts)
    }
}