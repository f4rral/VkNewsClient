package com.vknewsclient.data.mapper

import com.vknewsclient.data.model.newsFeed.NewsFeedGetDto
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem
import com.vknewsclient.domain.StatisticType
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.absoluteValue

class NewsFeedMapper {
    fun mapResponseToPostDto(httpGet: NewsFeedGetDto): List<FeedPost> {
        val response = httpGet.response
        val result = mutableListOf<FeedPost>()

        val posts = response.items
        val groups = response.groups

        for(post in posts) {
            if (post.id == null) {
                continue
            }
            val group = groups.find { it.id == post.communityId.absoluteValue } ?: continue

            val feedPost = FeedPost(
                id = post.id,
                communityId = post.communityId,
                communityName = group.name,
                communityImgUrl = group.photoUrl200,
                publicationDate = mapTimestampToDate(post.date * 1000),
                contentText = post.text ?: "",
                contentImageResUrl = post.attachments?.firstOrNull { it.type == "photo" }?.photo?.sizes?.lastOrNull()?.url,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = post.likes.count),
                    StatisticItem(type = StatisticType.VIEWS, count = post.views?.count ?: 0),
                    StatisticItem(type = StatisticType.SHARES, count = post.reposts?.count ?: 0),
                    StatisticItem(type = StatisticType.COMMENTS, count = post.comments?.count ?: 0),
                ),
                isLiked = post.likes.userLikes > 0
            )

            result.add(feedPost)
        }

        return result.distinctBy { it.id }
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)

        return SimpleDateFormat("dd MMMM yyyy, hh:mm", Locale.getDefault())
            .format(date)
    }
}