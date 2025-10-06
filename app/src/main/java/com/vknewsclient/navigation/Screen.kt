package com.vknewsclient.navigation

import com.google.gson.Gson
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.extensions.encode

sealed class Screen(
    val route: String
) {
    object Home: Screen(ROUTE_HOME)

    object Comments: Screen(ROUTE_COMMENTS) {
        private const val ROUTE_FOR_ARGS = "comments"

        fun getRouteWithArgs(feedPost: FeedPost): String {
            val feedPostJson = Gson().toJson(feedPost)
            return "$ROUTE_FOR_ARGS/${feedPostJson.encode()}"
        }
    }

    object NewsFeed: Screen(ROUTE_NEWS_FEED)
    object Favorite: Screen(ROUTE_FAVORITE)
    object Profile: Screen(ROUTE_PROFILE)
    object Gifts: Screen(ROUTE_GIFTS)

    companion object {
        const val KEY_FEED_POST = "feed_post"

        const val ROUTE_HOME = "home"
        const val ROUTE_COMMENTS = "comments/{$KEY_FEED_POST}"
        const val ROUTE_NEWS_FEED = "news_feed"
        const val ROUTE_FAVORITE = "favorite"
        const val ROUTE_PROFILE = "profile"
        const val ROUTE_GIFTS = "gifts"
    }
}

