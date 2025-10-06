package com.vknewsclient.data.mapper

import com.vknewsclient.data.model.gift.GiftsResponseDto
import com.vknewsclient.domain.Gift
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class GiftMapper {

    fun mapResponseToGifts(responseDto: GiftsResponseDto): List<Gift> {
        val result = mutableListOf<Gift>()

        val giftsUser = responseDto.giftsContent.items

        for(giftUser in giftsUser) {
            val gift = Gift(
                id = giftUser.id,
                date = mapTimestampToDate(giftUser.date * 1000),
                thumbUrl = giftUser.gift.thumb256,
                message = giftUser.message
            )

            result.add(gift)
        }

        return result
    }

    private fun mapTimestampToDate(timestamp: Long): String {
        val date = Date(timestamp)

        return SimpleDateFormat("d MMMM yyyy, hh:mm", Locale.getDefault()).format(date)
    }
}