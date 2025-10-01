package com.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.livedata.observeAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.vk.id.VKID
import com.vknewsclient.state.AuthState
import com.vknewsclient.ui.screens.LoginScreen
import com.vknewsclient.ui.screens.MainScreen
import com.vknewsclient.ui.theme.VkNewsClientTheme
import com.vknewsclient.viewModels.MainViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        VKID.init(this)
        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme {
                val viewModel: MainViewModel = viewModel()
                val authState = viewModel.authState.observeAsState(AuthState.Initial)

                when (authState.value) {
                    is AuthState.Authorized -> {
                        MainScreen()
                    }

                    is AuthState.NotAuthorized -> {
                        LoginScreen(
                            onLoginClick = {
                                viewModel.auth()
                            }
                        )
                    }

                    else -> {}
                }
            }
        }
    }
}
