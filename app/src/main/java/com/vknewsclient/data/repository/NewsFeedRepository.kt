package com.vknewsclient.data.repository

import android.util.Log
import com.vk.id.VKID
import com.vknewsclient.data.mapper.NewsFeedMapper
import com.vknewsclient.data.model.likes.LikesCountGetDto
import com.vknewsclient.data.model.likes.LikesCountResponseDto
import com.vknewsclient.data.network.ApiFactory
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.PostComment
import com.vknewsclient.domain.StatisticItem
import com.vknewsclient.domain.StatisticType
import com.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class NewsFeedRepository {

    private val _accessToken = VKID.instance.accessToken

    private val coroutineScope = CoroutineScope(Dispatchers.Default)
    private val nextDataNeededEvents = MutableSharedFlow<Unit>(replay = 1)
    private val refreshedListFlow = MutableSharedFlow<List<FeedPost>>()

    private val loadedListFlow = flow {
        nextDataNeededEvents.emit(Unit)

        nextDataNeededEvents.collect {
            val startFrom = _nextFrom

            if (startFrom == null && feedPosts.isNotEmpty()) {
                emit(feedPosts)
                return@collect
            }

            val httpGet = if (startFrom == null) {
                _apiService.loadNewsFeed(token = getAccessToken())
            } else {
                _apiService.loadNewsFeed(
                    token = getAccessToken(),
                    startFrom = startFrom
                )
            }

            val posts = _mapper.mapResponseToPost(httpGet)

            _nextFrom = httpGet.response.nextFrom
            _feedPosts.apply {
                addAll(posts)
                distinct()
            }

            _feedPosts.forEach {
                Log.d("feedPosts id", it.id.toString())
            }

            emit(feedPosts)
        }
    }

    private val _apiService = ApiFactory.apiService
    private val _mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var _nextFrom: String? = null

    val newsFeedFlow: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

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
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun deletePost(feedPost: FeedPost) {
        _apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            itemId = feedPost.id
        )

        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    suspend fun getComments(feedPost: FeedPost): List<PostComment> {
        val comments = _apiService.loadComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            itemId = feedPost.id
        )

        return _mapper.mapResponseToPostComments(comments)
    }

    suspend fun loadNextData() {
        nextDataNeededEvents.emit(Unit)
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
