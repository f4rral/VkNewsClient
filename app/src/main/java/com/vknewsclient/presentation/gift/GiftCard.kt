package com.vknewsclient.presentation.gift

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.vknewsclient.domain.Gift
import com.vknewsclient.ui.theme.VkNewsClientTheme

@Composable
fun GiftCard(
    gift: Gift
) {
    Card {
        Column(
            modifier = Modifier
                .padding(8.dp)
        ) {

            Text(
                text = gift.date,
                color = MaterialTheme.colorScheme.onSecondary
            )

            Spacer(modifier = Modifier.height(8.dp))

            Image(
                painter = painterResource(gift.thumbUrl),
                contentDescription = null,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = gift.message,
            )
        }
    }
}

@Preview
@Composable
fun PreviewLightGiftCard() {
    VkNewsClientTheme(darkTheme = true) {
        GiftCard(
            gift = Gift()
        )
    }
}

@Preview
@Composable
fun PreviewDarkGiftCard() {
    VkNewsClientTheme(darkTheme = false) {
        GiftCard(
            gift = Gift()
        )
    }
}