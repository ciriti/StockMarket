package com.ciriti.stockmarket.di

import com.ciriti.stockmarket.BuildConfig
import com.ciriti.stockmarket.R
import com.ciriti.stockmarket.data.* // ktlint-disable
import com.ciriti.stockmarket.data.utils.Logger
import com.ciriti.stockmarket.data.utils.debugLogger
import com.ciriti.stockmarket.ui.stockprice.StockPriceViewModel
import com.ciriti.stockmarket.ui.stockprice.StockPriceViewModelOkHttp
import com.ciriti.stockmarket.ui.stockprice.StockPriceViewModelRxJava
import com.ciriti.stockmarket.ui.stockprice.uicomponent.GDAXAdapter
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.streamadapter.rxjava2.RxJava2StreamAdapterFactory
//import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.util.concurrent.TimeUnit

object Constants {
    const val WEB_SOCKET_SERVICE_RXJAVA = "RxSocketService"
    const val WEB_SOCKET_SERVICE_IMPL = "WebSocketServiceImpl"
    const val WEB_SOCKET_SERVICE_SCARLET_IMPL = "WebSocketServiceScarletImpl"
}

val stockPriceModule = module {

    factory { GDAXAdapter() }

    viewModel<StockPriceViewModelRxJava> {
        StockPriceViewModelRxJava(
            service = get(qualifier = named(Constants.WEB_SOCKET_SERVICE_RXJAVA)),
            errorHandler = { throwable -> R.string.error }, // in a real situation this function would be different
            logger = get<Logger>()
        )
    }

    viewModel<StockPriceViewModelOkHttp> {
        StockPriceViewModelOkHttp(
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

//    single<RxSocketService>(qualifier = named(Constants.WEB_SOCKET_SERVICE_RXJAVA)) {
//
//        val lifecycle = AndroidLifecycle.ofApplicationForeground(androidApplication())
//
//        val okHttpClient = get<OkHttpClient>()
//
//        Scarlet.Builder()
//            .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.SOCKET_URL))
//            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
//            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
//            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
//            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 5000))
//            .lifecycle(lifecycle)
//            .build()
//            .create()
//    }

//    single<StockService> {
//
//        val lifecycle = AndroidLifecycle.ofApplicationForeground(androidApplication())
//
//        val okHttpClient = get<OkHttpClient>()
//
//        Scarlet.Builder()
//            .webSocketFactory(okHttpClient.newWebSocketFactory(BuildConfig.SOCKET_URL))
//            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
//            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
//            .addStreamAdapterFactory(RxJava2StreamAdapterFactory())
//            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 5000))
//            .lifecycle(lifecycle)
//            .build()
//            .create()
//    }

    single<OkHttpClient> {
        OkHttpClient.Builder()
            .writeTimeout(3000, TimeUnit.MILLISECONDS)
            .readTimeout(3000, TimeUnit.MILLISECONDS)
            .build()
    }

    single(qualifier = named(Constants.WEB_SOCKET_SERVICE_IMPL)) {
        val okHttpClient = get<OkHttpClient>()
        WebSocketService.creteGdax(okHttpClient)
    }

    single(qualifier = named(Constants.WEB_SOCKET_SERVICE_SCARLET_IMPL)) {
        val okHttpClient = get<OkHttpClient>()
        WebSocketService.crete(okHttpClient)
    }
}
