package com.vknewsclient.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vknewsclient.MainViewModel
import com.vknewsclient.data.NavigationItem

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val selectedNavItem by viewModel.selectedNavItem.observeAsState(NavigationItem.Home)

    Scaffold(
        bottomBar = {
            NavigationBar {
                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favorites,
                    NavigationItem.Profile,
                )

                items.forEach { item ->
                    NavigationBarItem(
                        label = {
                            Text(text = stringResource(item.titleResId))
                        },
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = null
                            )
                        },
                        selected = selectedNavItem == item,
                        onClick = { viewModel.selectedNavItem(item) },
                        colors = NavigationBarItemColors(
                            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
                            selectedIconColor = MaterialTheme.colorScheme.onPrimary,
                            selectedIndicatorColor = Color.Transparent,
                            unselectedIconColor = MaterialTheme.colorScheme.onSecondary,
                            unselectedTextColor = MaterialTheme.colorScheme.onSecondary,
                            disabledIconColor = MaterialTheme.colorScheme.onSecondary,
                            disabledTextColor = MaterialTheme.colorScheme.onSecondary
                        )
                    )
                }
            }
        },
    ) { innerPadding ->
        when(selectedNavItem) {
            NavigationItem.Home -> {
                HomeScreen(
                    viewModel = viewModel,
                    innerPadding = innerPadding
                )
            }

            NavigationItem.Favorites -> {
                TextCounter(
                    name = "Favorites",
                    innerPadding = innerPadding
                )
            }

            NavigationItem.Profile -> {
                TextCounter(
                    name = "Profile",
                    innerPadding = innerPadding
                )
            }
        }
    }
}

@Composable
private fun TextCounter(name: String, innerPadding: PaddingValues) {
    var count by remember {
        mutableIntStateOf(0)
    }

    Text(
        text = "$name Count: $count",
        color = Color.Black,
        modifier = Modifier
            .padding(innerPadding)
            .clickable { count++ }
    )
}