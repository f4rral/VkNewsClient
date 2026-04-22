package com.vknewsclient.data.repository

import android.util.Log
import com.vk.id.VKID
import com.vknewsclient.data.mapper.NewsFeedMapper
import com.vknewsclient.data.model.likes.LikesCountGetDto
import com.vknewsclient.data.model.likes.LikesCountResponseDto
import com.vknewsclient.data.network.ApiFactory
import com.vknewsclient.domain.entity.FeedPost
import com.vknewsclient.domain.entity.PostComment
import com.vknewsclient.domain.entity.StatisticItem
import com.vknewsclient.domain.entity.StatisticType
import com.vknewsclient.domain.repository.NewsFeedRepository
import com.vknewsclient.extensions.mergeWith
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.retry
import kotlinx.coroutines.flow.stateIn
import kotlin.random.Random

class NewsFeedRepositoryImpl: NewsFeedRepository {

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
    }.retry(retries = 2) {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.catch { }

    private val _apiService = ApiFactory.apiService
    private val _mapper = NewsFeedMapper()

    private val _feedPosts = mutableListOf<FeedPost>()
    private val feedPosts: List<FeedPost>
        get() = _feedPosts.toList()

    private var _nextFrom: String? = null

    private val newsFeedFlow: StateFlow<List<FeedPost>> = loadedListFlow
        .mergeWith(refreshedListFlow)
        .stateIn(
            scope = coroutineScope,
            started = SharingStarted.Lazily,
            initialValue = feedPosts
        )

    override suspend fun changeLikeStatus(feedPost: FeedPost) {
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

    override suspend fun deletePost(feedPost: FeedPost) {
        _apiService.ignorePost(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            itemId = feedPost.id
        )

        _feedPosts.remove(feedPost)
        refreshedListFlow.emit(feedPosts)
    }

    override fun getNewsFeed(): StateFlow<List<FeedPost>> {
        return newsFeedFlow
    }

    override fun getComments(feedPost: FeedPost): StateFlow<List<PostComment>> = flow {
        val comments = _apiService.loadComments(
            token = getAccessToken(),
            ownerId = feedPost.communityId,
            itemId = feedPost.id
        )

        emit(_mapper.mapResponseToPostComments(comments))
    }.retry {
        delay(RETRY_TIMEOUT_MILLIS)
        true
    }.stateIn(
        scope = coroutineScope,
        started = SharingStarted.Lazily,
        initialValue = listOf()
    )

    override suspend fun loadNextData() {
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

    companion object {

        private const val RETRY_TIMEOUT_MILLIS = 3000L
    }
}
