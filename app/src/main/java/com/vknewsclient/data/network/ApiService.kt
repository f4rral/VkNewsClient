package com.vknewsclient.data.network

import com.vknewsclient.data.model.comments.CommentsGetDto
import com.vknewsclient.data.model.gift.GiftsResponseDto
import com.vknewsclient.data.model.likes.LikesCountGetDto
import com.vknewsclient.data.model.newsFeed.NewsFeedGetDto
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("gifts.get?v=5.199")
    suspend fun loadGifts(
        @Query("access_token") token: String
    ): GiftsResponseDto

    @GET("newsfeed.get?v=5.199")
    suspend fun loadNewsFeed(
        @Query("access_token") token: String
    ): NewsFeedGetDto

    @GET("newsfeed.get?v=5.199")
    suspend fun loadNewsFeed(
        @Query("access_token") token: String,
        @Query("start_from") startFrom: String
    ): NewsFeedGetDto

    @POST("likes.add?v=5.199&type=post")
    suspend fun addLike(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long,
    ): LikesCountGetDto

//    @POST("likes.add?v=5.199&type=post")
//    suspend fun deleteLike(
//        @Query("access_token") token: String,
//        @Query("owner_id") ownerId: Long,
//        @Query("item_id") itemId: Long,
//    ): NewsFeedGetDto

    @GET("newsfeed.ignoreItem?v=5.199")
    suspend fun ignorePost(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("item_id") itemId: Long,
    )

    @GET("wall.getComments?v=5.199&extended=1&fields=photo_100")
    suspend fun loadComments(
        @Query("access_token") token: String,
        @Query("owner_id") ownerId: Long,
        @Query("post_id") itemId: Long,
    ): CommentsGetDto
}