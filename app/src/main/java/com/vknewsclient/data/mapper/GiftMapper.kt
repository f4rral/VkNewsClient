package com.vknewsclient.data.mapper

import com.vknewsclient.data.model.gift.GiftsResponseDto
import com.vknewsclient.domain.Gift

class GiftMapper {

    fun mapResponseToGifts(responseDto: GiftsResponseDto): List<Gift> {
        val result = mutableListOf<Gift>()

        val giftsUser = responseDto.giftsContent.items

        for(giftUser in giftsUser) {
            val gift = Gift(
                id = giftUser.id,
                date = giftUser.date.toString(),
                thumbUrl = giftUser.gift.thumb256,
                message = giftUser.message
            )

            result.add(gift)
        }

        return result
    }
}