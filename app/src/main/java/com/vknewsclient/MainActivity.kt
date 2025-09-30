package com.vknewsclient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vknewsclient.ui.screens.ActivityResultJC
import com.vknewsclient.ui.screens.MainScreen
import com.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VkNewsClientTheme {
                ActivityResultJC()
            }
        }
    }
}
