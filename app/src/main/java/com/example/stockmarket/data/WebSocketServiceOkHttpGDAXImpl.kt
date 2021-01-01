package com.example.stockmarket.data

import com.example.stockmarket.BuildConfig
import com.example.stockmarket.ui.stockprice.check
import com.google.gson.Gson
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.cancel
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import okhttp3.*

fun WebSocketService.Companion.creteGdax(client: OkHttpClient): WebSocketService =
    WebSocketServiceOkHttpGDAXImpl(client)

class WebSocketServiceOkHttpGDAXImpl(private val client: OkHttpClient) :
    WebSocketService {

    private val converter by lazy { Gson() }

    private var webS: WebSocket? = null

    override fun flowFrom(): Flow<StockInfo> = callbackFlow {

        val request = Request.Builder()
            .url(BuildConfig.SOCKET_URL)
            .build()
        webS = client.newWebSocket(request) {
            onOpen { webSocket, response -> subscribeAll(webSocket) }
            onMessage { webSocket, text ->
                check { text.toGDAXInfo().toStockInfo() }
                .fold(
                    { e -> e.printStackTrace() },
                    { stock -> sendBlocking(stock) }
                )
            }
            onMessageByte { webSocket, bytes -> println("=> MESSAGE: " + bytes.hex()) }
            onClosing { webSocket, code, reason ->
                unSubscribeAll(webSocket)
//                webSocket.close(1000, null)
                println("CLOSE: $code $reason")
                channel.close()
            }
            onClosed { webSocket, code, reason -> }
            onFailure { webSocket, t, response ->
                t.printStackTrace()
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
        webSocket.send(UnSubscribeCommandGDAX().toString())
        webS?.close(1000, "Goodbye, World!")
    }

    override fun unSubscribeAll() {
        webS?.send(UnSubscribeCommandGDAX().toString())
        webS?.close(1000, "Goodbye, World!")
    }

    private fun subscribeAll(webSocket: WebSocket) {
        webSocket.send(SubscribeCommandGDAX(gdaxList).toString())
    }

    private fun String.toGDAXInfo() = converter.fromJson<GDAXInfo>(this, GDAXInfo::class.java)
    private fun GDAXInfo.toStockInfo() = StockInfo(isin = product_id, price = price)


}