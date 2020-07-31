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
import okio.ByteString

fun WebSocketService.Companion.crete(client: OkHttpClient) : WebSocketService =
    WebSocketServiceImpl(client)

class WebSocketServiceImpl(private val client: OkHttpClient) :
    WebSocketService {

    private val converter by lazy { Gson() }

    private var webS: WebSocket? = null

    override fun flowFrom(): Flow<StockInfo> = callbackFlow {

        val request = Request.Builder()
            .url(BuildConfig.SOCKET_URL)
            .build()

        client.newWebSocket(request, object : WebSocketListener() {

            override fun onOpen(
                webSocket: WebSocket,
                response: Response
            ) {
                webS = webSocket
                subscribeAll(webSocket)
                println("OPEN")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                val stockInfo: StockInfo = text.toStockInfo()
                sendBlocking(stockInfo)
            }

            override fun onMessage(
                webSocket: WebSocket,
                bytes: ByteString
            ) {
                println("=> MESSAGE: " + bytes.hex())
            }

            override fun onClosing(
                webSocket: WebSocket,
                code: Int,
                reason: String
            ) {
                unSubscribeAll(webSocket)
//                webSocket.close(1000, null)
                println("CLOSE: $code $reason")
                channel.close()
            }

            override fun onFailure(
                webSocket: WebSocket,
                t: Throwable,
                response: Response?
            ) {
                println("Failure: ${t.printStackTrace()}")
                cancel(CancellationException("API Error", t))
            }
        })

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