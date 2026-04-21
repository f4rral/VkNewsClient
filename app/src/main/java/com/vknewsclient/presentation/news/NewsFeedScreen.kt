package com.vknewsclient.presentation.news

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.ui.theme.VKMainColor

@Composable
fun NewsFeedScreen(
    innerPadding: PaddingValues,
    onCommentClickListener: (feedPost: FeedPost) -> Unit
) {
    val viewModel: NewsFeedViewModel = viewModel()

    val screenState = viewModel.screenState.collectAsState(NewsFeedScreenState.Initial)
    val currentState = screenState.value

    when(currentState) {
        is NewsFeedScreenState.Posts -> {
            FeedPosts(
                posts = currentState.posts,
                viewModel = viewModel,
                innerPadding = innerPadding,
                nextDataIsLoading = currentState.nextDataIsLoading,
                onCommentClickListener = onCommentClickListener,
            )
        }

        is NewsFeedScreenState.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(
                    color = VKMainColor
                )
            }
        }

        NewsFeedScreenState.Initial -> {}
    }
}

@Composable
private fun FeedPosts(
    posts: List<FeedPost>,
    viewModel: NewsFeedViewModel,
    innerPadding: PaddingValues,
    nextDataIsLoading: Boolean = false,
    onCommentClickListener: (feedPost: FeedPost) -> Unit,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(innerPadding)
    ) {
        items(
            items = posts,
            key = { it.id }
        ) { feedPost ->
            val dismissState = rememberSwipeToDismissBoxState(
                confirmValueChange = {
                    if (it == SwipeToDismissBoxValue.EndToStart) {
                        viewModel.remove(feedPost)

                        true
                    }

                    false
                }
            )

            SwipeToDismissBox(
                state = dismissState,
                backgroundContent = {},
                enableDismissFromStartToEnd = false,
                enableDismissFromEndToStart = true,
                modifier = Modifier.animateItem()
            ) {
                PostCard(
                    feedPost = feedPost,
                    onCommentClickListener =  {
                        onCommentClickListener(feedPost)
                    },
                    onLikeClickListener =  { _ ->
                        viewModel.changeLikeStatus(feedPost = feedPost)
                    },
                )
            }
        }

        item {
            if (nextDataIsLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(
                        color = VKMainColor
                    )
                }
            } else {
                SideEffect {
                    viewModel.loadNextNewsFeed()
                }
            }
        }
    }
}