package com.example.stockmarket.di

import com.example.stockmarket.*
import com.example.stockmarket.data.StockService
import com.example.stockmarket.data.WebSocketService
import com.example.stockmarket.data.crete
import com.example.stockmarket.ui.stockprice.StockPriceViewModel
import com.example.stockmarket.ui.stockprice.StockPriceViewModel2
import com.example.stockmarket.utils.Logger
import com.example.stockmarket.utils.debugLogger
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object Constants{
    const val WEB_SOCKET_SERVICE_IMPL = "WebSocketServiceImpl"
    const val WEB_SOCKET_SERVICE_SCARLET_IMPL = "WebSocketServiceScarletImpl"
}

val stockPriceModule = module {

    viewModel<StockPriceViewModel2> {
        StockPriceViewModel2(
            service = get(qualifier = named(Constants.WEB_SOCKET_SERVICE_IMPL)),
            errorHandler = { throwable -> R.string.error }, // in a real situation this function would be different
            logger = get<Logger>()
        )
    }

    viewModel<StockPriceViewModel> {
        StockPriceViewModel(
            service = get<StockService>(),
            errorHandler = { throwable -> R.string.error }, // in a real situation this function would be different
            logger = get<Logger>()
        )
    }

    single<Logger> { Logger.debugLogger() }

    single<StockService> {

        val lifecycle = AndroidLifecycle.ofApplicationForeground(androidApplication())

        val okHttpClient = get<OkHttpClient>()

        Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.SOCKET_URL))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 5000))
            .lifecycle(lifecycle)
            .build()
            .create()
    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build()
    }

    single(qualifier = named(Constants.WEB_SOCKET_SERVICE_IMPL)) {
        val okHttpClient = get<OkHttpClient>()
        WebSocketService.crete(okHttpClient)
    }

    single(qualifier = named(Constants.WEB_SOCKET_SERVICE_SCARLET_IMPL)) {
        val okHttpClient = get<OkHttpClient>()
        WebSocketService.crete(okHttpClient)
    }

}