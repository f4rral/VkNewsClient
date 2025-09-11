package com.vknewsclient.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.vknewsclient.MainViewModel
import com.vknewsclient.ui.PostCard

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    innerPadding: PaddingValues
) {
    val feedPosts = viewModel.feedPosts.observeAsState(listOf())

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
            items = feedPosts.value,
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
                    onViewsClickListener = { statisticItem ->
                        viewModel.updateCount(
                            feedPost = feedPost,
                            statisticItem = statisticItem
                        )
                    },
                    onShareClickListener =  { statisticItem ->
                        viewModel.updateCount(
                            feedPost = feedPost,
                            statisticItem = statisticItem
                        )
                    },
                    onCommentClickListener =  { statisticItem ->
                        viewModel.updateCount(
                            feedPost = feedPost,
                            statisticItem = statisticItem
                        )
                    },
                    onLikeClickListener =  { statisticItem ->
                        viewModel.updateCount(
                            feedPost = feedPost,
                            statisticItem = statisticItem
                        )
                    },
                )
            }
        }
    }
}
