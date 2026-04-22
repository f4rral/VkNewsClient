package com.vknewsclient.presentation.news

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vknewsclient.data.repository.NewsFeedRepositoryImpl
import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.usecases.ChangeLikeStatusUseCase
import com.vknewsclient.domain.usecases.DeletePostUseCase
import com.vknewsclient.domain.usecases.GetNewsFeedUseCase
import com.vknewsclient.domain.usecases.LoadNextDataUseCase
import com.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class NewsFeedViewModel: ViewModel() {

    private val exceptionHandler = CoroutineExceptionHandler {_, _ ->
        Log.d("NewsFeedViewModel", "Exception caught by exception handler")
    }

    private val repository = NewsFeedRepositoryImpl()

    private val getNewsFeedUseCase = GetNewsFeedUseCase(repository)
    private val loadNextDataUseCase = LoadNextDataUseCase(repository)
    private val changeLikeStatusUseCase = ChangeLikeStatusUseCase(repository)
    private val deletePostUseCase = DeletePostUseCase(repository)

    private val newsFeedFlow = getNewsFeedUseCase()
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

            loadNextDataUseCase()
        }
    }

    fun changeLikeStatus(feedPost: FeedPost) {
        viewModelScope.launch(exceptionHandler) {
            changeLikeStatusUseCase(feedPost)
        }
    }

    fun remove(feedPost: FeedPost) {
       viewModelScope.launch(exceptionHandler) {
           deletePostUseCase(feedPost)
       }
    }
}