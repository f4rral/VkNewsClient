package com.vknewsclient.data.repository

import android.util.Log
import com.vk.id.VKID
import com.vknewsclient.data.mapper.NewsFeedMapper
import com.vknewsclient.data.model.likes.LikesCountGetDto
import com.vknewsclient.data.model.likes.LikesCountResponseDto
import com.vknewsclient.data.network.ApiFactory
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem
import com.vknewsclient.domain.StatisticType
import kotlin.random.Random

class NewsFeedRepository {
    private val _accessToken = VKID.instance.accessToken
    private val _apiService = ApiFactory.apiService
    private val _mapper = NewsFeedMapper()
    private val _feedPosts = mutableListOf<FeedPost>()
    val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var _nextFrom: String? = null

    suspend fun loadNewsFeed(): List<FeedPost> {
        val startFrom = _nextFrom

        if (startFrom == null && feedPosts.isNotEmpty()) {
            return feedPosts
        }

        val httpGet = if (startFrom == null) {
            _apiService.loadNewsFeed(token = getAccessToken())
        } else {
            _apiService.loadNewsFeed(
                token = getAccessToken(),
                startFrom = startFrom
            )
        }

        val posts = _mapper.mapResponseToPostDto(httpGet)

        _nextFrom = httpGet.response.nextFrom
        _feedPosts.apply {
            addAll(posts)
            distinct()
        }

        _feedPosts.forEach {
            Log.d("feedPosts id", it.id.toString())
        }

        return feedPosts
    }

    suspend fun changeLikeStatus(feedPost: FeedPost) {
//        val httpGet = apiService.addLike(
//            token = fakeToken,
//            ownerId = feedPost.communityId,
//            itemId = feedPost.id,
//        )

        val httpGet = fakeGetLikes()

        val newLikesCount = httpGet.response.likes
        val indexPost = _feedPosts.indexOf(feedPost)

        val newStatistics = feedPost.statistics.toMutableList().apply {
            removeIf { it.type == StatisticType.LIKES }

            add(StatisticItem(
                type = StatisticType.LIKES,
                count = newLikesCount
            ))
        }

        val newPost = feedPost.copy(
            statistics = newStatistics,
            isLiked = !feedPost.isLiked
        )

        _feedPosts[indexPost] = newPost
    }

    private fun getAccessToken(): String {
        return _accessToken?.token ?: throw IllegalStateException("AccessToken is null")
    }

    // Фейковые данные для добавления/удаления лайков,
    // т.к. VK API перестали работать методы likes.add/likes.delete
    private fun fakeGetLikes(): LikesCountGetDto {
        return LikesCountGetDto(
            response = LikesCountResponseDto(
                likes = Random.nextInt(5, 10)
            )
        )
    }
}
