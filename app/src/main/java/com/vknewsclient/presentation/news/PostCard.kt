package com.vknewsclient.presentation.news

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.vknewsclient.R
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.domain.StatisticItem
import com.vknewsclient.domain.StatisticType
import com.vknewsclient.domain.getItemByType
import com.vknewsclient.ui.theme.DarkRed
import com.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun PostCard(
    modifier: Modifier = Modifier,
    feedPost: FeedPost,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit,
) {
    Card(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {
            PostHeader(feedPost)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = feedPost.contentText
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (feedPost.contentImageResUrl != null) {
                AsyncImage(
                    model = feedPost.contentImageResUrl,
                    contentDescription = null,
                    contentScale = ContentScale.FillWidth,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))
            }

            Statistics(
                statistics = feedPost.statistics,
                onCommentClickListener = onCommentClickListener,
                onLikeClickListener = onLikeClickListener,
                isLiked = feedPost.isLiked,
            )
        }
    }
}

@Composable
private fun PostHeader(
    feedPost: FeedPost
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
    ) {
        AsyncImage(
            model = feedPost.communityImgUrl,
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape),
            contentDescription = null
        )

        Spacer(
            modifier = Modifier.width(8.dp)
        )

        Column(
            modifier = Modifier.weight(weight = 1f)
        ) {
            Text(
                text = feedPost.communityName,
                color = MaterialTheme.colorScheme.onPrimary
            )

            Spacer(
                modifier = Modifier.height(4.dp)
            )

            Text(
                text = feedPost.publicationDate,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }

        Icon(
            imageVector = Icons.Rounded.MoreVert,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSecondary
        )
    }
}

@Composable
private fun Statistics(
    statistics: List<StatisticItem>,
    onCommentClickListener: (StatisticItem) -> Unit,
    onLikeClickListener: (StatisticItem) -> Unit,
    isLiked: Boolean = true,
) {
    Row {
        Row(
            modifier = Modifier
                .weight(1f)
        ) {
            val viewsItem = statistics.getItemByType(StatisticType.VIEWS)
            IconWithText(
                iconResId = R.drawable.ic_views_count,
                text = formatStatisticCount(viewsItem.count),
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .weight(1f)
        ) {
            val sharesItem = statistics.getItemByType(StatisticType.SHARES)
            IconWithText(
                iconResId = R.drawable.ic_share,
                text = formatStatisticCount(sharesItem.count),
            )

            val commentsItem = statistics.getItemByType(StatisticType.COMMENTS)
            IconWithText(
                iconResId = R.drawable.ic_comment,
                text = formatStatisticCount(commentsItem.count),
                onItemClickListener = {
                    onCommentClickListener(commentsItem)
                }
            )

            val likesItem = statistics.getItemByType(StatisticType.LIKES)
            IconWithText(
                iconResId = if (isLiked) R.drawable.ic_like_set else R.drawable.ic_like,
                tint = if (isLiked) DarkRed else MaterialTheme.colorScheme.onSecondary,
                text = formatStatisticCount(likesItem.count),
                onItemClickListener = {
                    onLikeClickListener(likesItem)
                }
            )
        }
    }
}

private fun formatStatisticCount(count: Int): String {
    return if (count > 100_000) {
        String.format("%sK", (count / 1000))
    } else if (count > 1000) {
        String.format("%.1fK", (count / 1000f))
    } else {
        count.toString()
    }
}

@Composable
private fun IconWithText(
    iconResId: Int,
    text: String,
    onItemClickListener: (() -> Unit)? = null,
    tint: Color = MaterialTheme.colorScheme.onSecondary
) {
    val modifier = if (onItemClickListener == null) {
        Modifier
    } else {
        Modifier.clickable {
            onItemClickListener()
        }
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier,
    ) {
        Icon(
            modifier = Modifier.size(20.dp),
            painter = painterResource(iconResId),
            contentDescription = null,
            tint = tint
        )

        Spacer(modifier = Modifier.width(4.dp))

        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondary
        )
    }
}
// изображений
@Preview
@Composable
fun PreviewLightPostCard() {
    VkNewsClientTheme(darkTheme = true) {
        PostCard(
            feedPost = FeedPost(
                id = 1,
                communityId = 1,
                communityName = "communityName",
                communityImgUrl = "",
                publicationDate = 1773415043.toString(),
                contentText = "contentText",
                contentImageResUrl = "",
                isLiked = false,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = 99),
                    StatisticItem(type = StatisticType.COMMENTS, count = 999),
                    StatisticItem(type = StatisticType.SHARES, count = 9999),
                    StatisticItem(type = StatisticType.VIEWS, count = 999999)
                )
            ),
            onCommentClickListener = { },
            onLikeClickListener = { },
        )
    }
}

@Preview
@Composable
fun PreviewDarkPostCard() {
    VkNewsClientTheme(darkTheme = false) {
        PostCard(
            feedPost = FeedPost(
                id = 1,
                communityId = 1,
                communityName = "communityName",
                publicationDate = 1773415043.toString(),
                communityImgUrl = "",
                contentText = "contentText",
                contentImageResUrl = "",
                isLiked = true,
                statistics = listOf(
                    StatisticItem(type = StatisticType.LIKES, count = 99),
                    StatisticItem(type = StatisticType.COMMENTS, count = 999),
                    StatisticItem(type = StatisticType.SHARES, count = 9999),
                    StatisticItem(type = StatisticType.VIEWS, count = 999999)
                )
            ),
            onCommentClickListener = { },
            onLikeClickListener = { },
        )
    }
}