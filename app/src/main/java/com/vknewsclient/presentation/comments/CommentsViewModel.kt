package com.vknewsclient.presentation.comments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.vknewsclient.data.repository.NewsFeedRepository
import com.vknewsclient.domain.FeedPost
import kotlinx.coroutines.launch

class CommentsViewModel(
    feedPost: FeedPost
): ViewModel() {
    private val _repository = NewsFeedRepository()
    private val _screenState = MutableLiveData<CommentsScreenState>(CommentsScreenState.Initial)
    val screenState: LiveData<CommentsScreenState> = _screenState

    init {
        loadComments(feedPost = feedPost)
    }

    fun loadComments(feedPost: FeedPost) {
        viewModelScope.launch {
            val comments = _repository.getComments(feedPost = feedPost)

            _screenState.value = CommentsScreenState.Comments(
                feedPost = feedPost,
                comments = comments
            )
        }
    }
}

class CommentsViewModelFactory(
    private val feedPost: FeedPost
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CommentsViewModel(feedPost) as T
    }
}