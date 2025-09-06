package com.vknewsclient

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem

class MainViewModel: ViewModel() {
    private val sourceList = mutableListOf<FeedPost>().apply {
        repeat(10) { index ->
            add(FeedPost(id = index))
        }
    }

    private val _feedPosts = MutableLiveData<List<FeedPost>>(sourceList)
    val feedPosts: LiveData<List<FeedPost>> = _feedPosts

    fun updateCount(feedPost: FeedPost, statisticItem: StatisticItem) {
        val oldPosts = feedPosts.value?.toMutableList() ?: mutableListOf()
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

        val newPost = feedPost.copy(statistics = newStatistics)

        _feedPosts.value = oldPosts.apply {
            replaceAll {
                if (it.id == newPost.id) {
                    newPost
                } else {
                    it
                }
            }
        }
    }

    fun remove(feedPost: FeedPost) {
        val oldPost = feedPosts.value?.toMutableList() ?: mutableListOf()
        oldPost.remove(feedPost)

        _feedPosts.value = oldPost
    }
}