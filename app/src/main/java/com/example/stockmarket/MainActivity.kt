package com.example.stockmarket

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.stockmarket.data.*
import com.example.stockmarket.utils.stocksList
import com.tinder.scarlet.Scarlet
import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.lifecycle.android.AndroidLifecycle
import com.tinder.scarlet.messageadapter.moshi.MoshiMessageAdapter
import com.tinder.scarlet.retry.ExponentialWithJitterBackoffStrategy
import com.tinder.scarlet.websocket.okhttp.newWebSocketFactory
import com.tinder.streamadapter.coroutines.CoroutinesStreamAdapterFactory
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.consumeEach
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import okhttp3.OkHttpClient
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val SOCKET_URL = "ws://159.89.15.214:8080/"

    val scope = MainScope()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val lifecycle = AndroidLifecycle.ofApplicationForeground(application)

        val okHttpClient = OkHttpClient.Builder()
            .writeTimeout(500, TimeUnit.MILLISECONDS)
            .readTimeout(500, TimeUnit.MILLISECONDS)
            .build()

        val scarlet = Scarlet.Builder()
            .webSocketFactory(okHttpClient.newWebSocketFactory(SOCKET_URL))
            .addMessageAdapterFactory(MoshiMessageAdapter.Factory())
            .addStreamAdapterFactory(CoroutinesStreamAdapterFactory())
            .backoffStrategy(ExponentialWithJitterBackoffStrategy(5000, 5000))
            .lifecycle(lifecycle)
            .build()
            .create<StockService>()

        scope.launch {
            scarlet.observeWebSocketEvent().consumeEach { event ->
                println("porcodio " + event)
                if(event is WebSocket.Event.OnConnectionOpened<*>){
                    stocksList.forEach {
                        scarlet.subscribe(SubscribeAction(it))
                    }

                    while(true){
                        println("porcodio " + scarlet.observeStock().receive())
                    }
                }
            }
//
        }



        tv.setOnClickListener {
            stocksList.forEach {
                scarlet.unSubscribe(UnSubscribe(it))
            }

        }

    }

    override fun onDestroy() {
        super.onDestroy()

    }
}