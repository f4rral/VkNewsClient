package com.vknewsclient.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.vknewsclient.domain.FeedPost

fun NavGraphBuilder.homeScreenNavGraph(
    newsFeedScreenContent: @Composable () -> Unit,
    commentsScreenContent: @Composable (feedPost: FeedPost) -> Unit,
) {
    navigation(
        startDestination = Screen.NewsFeed.route,
        route = Screen.Home.route
    ) {
        composable(route = Screen.NewsFeed.route) {
            newsFeedScreenContent()
        }

        composable(
            route = Screen.Comments.route,
            arguments = listOf(
                navArgument(name = Screen.KEY_FEED_POST) {
                    type = FeedPost.NavigationType
                },
            )
        ) {
            val feedPost = it.arguments?.getParcelable<FeedPost>(Screen.KEY_FEED_POST) ?: (
                throw RuntimeException("Argument feedPost is NULL")
            )

            commentsScreenContent(feedPost)
        }
    }
}