package com.vknewsclient.data.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

object ApiFactory {
    private const val BASE_URL = "https://api.vk.ru/method/"
    private val gson = GsonBuilder()
        .setPrettyPrinting()
        .create()

    private val loggingInterceptor = HttpLoggingInterceptor { message ->
        if (message.startsWith("{") || message.startsWith("[")) {
            try {
                val jsonElement = gson.fromJson(message, JsonElement::class.java)
                val prettyJson = gson.toJson(jsonElement)
                Log.d("OkHttp", prettyJson)
            } catch (e: Exception) {
                Log.d("OkHttp", message)
            }
        } else {
            Log.d("OkHttp", message)
        }
    }.apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(okHttpClient)
        .build()

    val apiService: ApiService = retrofit.create()
}