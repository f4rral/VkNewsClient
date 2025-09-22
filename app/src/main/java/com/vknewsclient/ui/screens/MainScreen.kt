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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.vknewsclient.MainViewModel
import com.vknewsclient.data.NavigationItem
import com.vknewsclient.navigation.AppNavGraph
import com.vknewsclient.navigation.Screen

@Composable
fun MainScreen(
    viewModel: MainViewModel
) {
    val navHostController = rememberNavController()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navHostController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route
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
                        selected = currentRoute == item.screen.route,
                        onClick = {
                            navHostController.navigate(route = item.screen.route) {
                                popUpTo(Screen.NewsFeed.route) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
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
            navHostController = navHostController,
            homeScreenContent = {
                HomeScreen(
                    viewModel = viewModel,
                    innerPadding = innerPadding
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