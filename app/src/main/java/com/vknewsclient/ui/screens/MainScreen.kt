package com.vknewsclient.ui.screens

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemColors
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.vknewsclient.data.NavigationItem
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    val snackBarHostState = SnackbarHostState()
    val scope = rememberCoroutineScope()
    val isFabVisible = remember {
        mutableStateOf(true)
    }

    Scaffold(
        bottomBar = {
            NavigationBar {
                val selectedItemPosition = remember {
                    mutableIntStateOf(0)
                }

                val items = listOf(
                    NavigationItem.Home,
                    NavigationItem.Favorites,
                    NavigationItem.Profile,
                )

                items.forEachIndexed { index, item ->
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
                        selected = selectedItemPosition.intValue == index,
                        onClick = { selectedItemPosition.intValue = index },
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

        floatingActionButton = {
            if (isFabVisible.value) {
                FloatingActionButton(
                    onClick = {
                        scope.launch {
                            val action = snackBarHostState.showSnackbar(
                                message = "Это Snackbar",
                                actionLabel = "Скрыть",
                                duration = SnackbarDuration.Long
                            )

                            if (action == SnackbarResult.ActionPerformed) {
                                isFabVisible.value = false
                            }
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Favorite,
                        contentDescription = null
                    )
                }
            }
        },

        snackbarHost = {
            SnackbarHost(
                hostState = snackBarHostState
            )
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding))
    }
}