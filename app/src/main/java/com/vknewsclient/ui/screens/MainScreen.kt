package com.vknewsclient.ui.screens

import android.util.Log
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
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.compose.currentBackStackEntryAsState
import com.vknewsclient.data.NavigationItem
import com.vknewsclient.domain.FeedPost
import com.vknewsclient.navigation.AppNavGraph
import com.vknewsclient.navigation.Screen
import com.vknewsclient.navigation.rememberNavigationState

@Composable
fun MainScreen() {
    val navigationState = rememberNavigationState()
    val commentsToPost: MutableState<FeedPost?> = remember {
        mutableStateOf(value = null)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navigationState.navHostController.currentBackStackEntryAsState()

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favorites,
                    NavigationItem.Profile,
                )

                items.forEach { item ->
                    val selected = navBackStackEntry?.destination?.hierarchy?.any {
                        it.route == item.screen.route
                    } ?: false

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
                        selected = selected,
                        onClick = {
                            if (!selected) {
                                navigationState.navigateTo(item.screen.route)
                            }
                        },
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
        AppNavGraph(
            navHostController = navigationState.navHostController,
            newsFeedScreenContent = {
                HomeScreen(
                    innerPadding = innerPadding,
                    onCommentClickListener = {
                        commentsToPost.value = it
                        navigationState.navigateToComments()
                    }
                )
            },
            favoriteScreenContent = {
                TextCounter(
                    name = "Favorites",
                    innerPadding = innerPadding
                )
            },
            profileScreenContent = {
                TextCounter(
                    name = "Profile",
                    innerPadding = innerPadding
                )
            },
            commentsScreenContent = {
                CommentsScreen(
                    feedPost = commentsToPost.value!!,
                    onBackPressed = {
                        navigationState.navHostController.popBackStack()
                    }
                )
            },
        )
    }
}

@Composable
private fun TextCounter(name: String, innerPadding: PaddingValues) {
    var count by rememberSaveable {
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