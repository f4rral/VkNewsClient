package com.vknewsclient.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vknewsclient.data.repository.NewsFeedRepository
import com.vknewsclient.domain.FeedPost
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost
): ViewModel() {
    private val _repository = NewsFeedRepository()
    val screenState = _repository.getComments(feedPost)
        .map {
            CommentsScreenState.Comments(
                comments = it,
                feedPost = feedPost
            )
        }
}

class CommentsViewModelFactory(
    private val feedPost: FeedPost
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost) as T
    }
}