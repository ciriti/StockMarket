package com.example.stockmarket.di

import com.example.stockmarket.BuildConfig
import com.example.stockmarket.data.StockService
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

val stockPriceModule = module {

    single<StockService> {

        val lifecycle = AndroidLifecycle.ofApplicationForeground(androidApplication())

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build()

        Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.SOCKET_URL))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 5000))
            .lifecycle(lifecycle)
            .build()
            .create()
    }



}