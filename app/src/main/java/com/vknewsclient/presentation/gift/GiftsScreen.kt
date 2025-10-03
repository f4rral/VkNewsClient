package com.vknewsclient.presentation.gift

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vknewsclient.domain.Gift

@Composable
fun GiftsScreen(
    innerPadding: PaddingValues,
) {
    val viewModel: GiftsViewModel = viewModel()
    val screenState = viewModel.screenState.observeAsState(GiftScreenState.Initial)
    val currentState = screenState.value

    when (currentState) {
        is GiftScreenState.Gifts -> {
            GiftsList(
                gifts = currentState.gifts,
                innerPadding = innerPadding
            )
        }
        is GiftScreenState.Initial -> {}
    }
}

@Composable
private fun GiftsList(
    gifts: List<Gift>,
    innerPadding: PaddingValues,
) {
    LazyColumn(
        contentPadding = PaddingValues(
            top = 16.dp,
            start = 8.dp,
            end = 8.dp,
            bottom = 16.dp
        ),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(innerPadding)
    ) {
        items(
            items = gifts,
            key = { it.id }
        ) {
            GiftCard(
                gift = it
            )
        }
    }
}