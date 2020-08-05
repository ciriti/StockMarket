package com.example.stockmarket.data

import com.example.stockmarket.BuildConfig
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*

fun WebSocketService.Companion.crete(client: OkHttpClient) : WebSocketService =
    WebSocketServiceOkHttpImpl(client)

class WebSocketServiceOkHttpImpl(private val client: OkHttpClient) :
    WebSocketService {

    private val converter by lazy { Gson() }

    private var webS: WebSocket? = null

    override fun flowFrom(): Flow<StockInfo> = callbackFlow {

        val request = Request.Builder()
            .url(BuildConfig.SOCKET_URL)
            .build()

        webS = client.newWebSocket(request){
            onOpen{ webSocket, response -> subscribeAll(webSocket) }
            onMessage { webSocket, text -> sendBlocking(text.toStockInfo()) }
            onMessageByte { webSocket, bytes -> println("=> MESSAGE: " + bytes.hex()) }
            onClosing { webSocket, code, reason ->
                unSubscribeAll(webSocket)
//                webSocket.close(1000, null)
                println("CLOSE: $code $reason")
                channel.close()
            }
            onClosed { webSocket, code, reason ->  }
            onFailure { webSocket, t, response ->
                println("Failure: ${t.printStackTrace()}")
                cancel(CancellationException("API Error", t))
            }
        }

        /*
         * Suspends until either 'onCompleted'/'onApiError' from the callback is invoked
         * or flow collector is cancelled (e.g. by 'take(1)' or because a collector's coroutine was cancelled).
         * In both cases, callback will be properly unregistered.
         */
        awaitClose {
            // Trigger shutdown of the dispatcher's executor so this process can exit cleanly.
            unSubscribeAll()
            webS?.close(1000, "Goodbye, World!")
//            client.dispatcher().executorService().shutdown()
        }


    }

    private fun unSubscribeAll(webSocket: WebSocket) {
        stockList.forEach {
            webSocket.send(UnSubscribeCommand(it).toJson())
        }
        webS?.close(1000, "Goodbye, World!")
    }

    override fun unSubscribeAll() {
        stockList.forEach {
            webS?.send(UnSubscribeCommand(it).toJson())
            webS?.close(1000, "Goodbye, World!")
        }
    }

    private fun subscribeAll(webSocket: WebSocket) {
        stockList.forEach {
            webSocket.send(SubscribeCommand(it).toJson())
        }
    }

    private fun SubscribeCommand.toJson() = converter.toJson(this)
    private fun UnSubscribeCommand.toJson() = converter.toJson(this)
    private fun String.toStockInfo() = converter.fromJson<StockInfo>(this, StockInfo::class.java)

}