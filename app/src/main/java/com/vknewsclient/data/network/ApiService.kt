package com.vknewsclient.data.network

import com.vknewsclient.data.model.gift.GiftsResponseDto
import com.vknewsclient.data.model.newsFeed.NewsFeedGetDto
import com.vknewsclient.data.model.newsFeed.NewsFeedResponseDto
import retrofit2.http.GET
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
}