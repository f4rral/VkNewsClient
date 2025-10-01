package com.vknewsclient

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.vk.id.AccessToken
import com.vk.id.VKID
import com.vk.id.VKIDAuthFail
import com.vk.id.auth.VKIDAuthCallback
import com.vknewsclient.ui.screens.ActivityResultJC
import com.vknewsclient.ui.screens.MainScreen
import com.vknewsclient.ui.theme.VkNewsClientTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        setContent {
            VKID.init(this)

            val vkAuthCallback = object : VKIDAuthCallback {
                override fun onAuth(accessToken: AccessToken) {
                    val token = accessToken.token

                    Log.d("MainActivity", "onAuth $token")
                }

                override fun onFail(fail: VKIDAuthFail) {
                    when (fail) {
                        is VKIDAuthFail.Canceled -> {
                            Log.d("MainActivity", "onFail VKIDAuthFail.Canceled")
                        }
                        else -> {
                            Log.d("MainActivity", "onFail")
                        }
                    }
                }
            }

            VKID.instance.authorize(this@MainActivity, vkAuthCallback)

            VkNewsClientTheme {
                ActivityResultJC()
            }
        }
    }
}
