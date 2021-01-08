package com.ciriti.stockmarket.data

import com.ciriti.stockmarket.utils.Logger
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.reactive.consumeEach

fun WebSocketService.Companion.scarletCrete(
    service: StockService,
    errorHandler: (Throwable) -> Int,
    logger: Logger,
    pStockList: List<String> = stockList
): WebSocketService =
    WebSocketServiceScarletImpl(
        service, errorHandler, logger, pStockList
    )

class WebSocketServiceScarletImpl(
    private val service: StockService,
    private val errorHandler: (Throwable) -> Int,
    private val logger: Logger,
    private val pStockList: List<String> = stockList
) :
    WebSocketService {

    override fun flowFrom(): Flow<StockInfo> = callbackFlow {

        service.observeWebSocketEvent().consumeEach { event ->
            when (event) {
                is com.tinder.scarlet.WebSocket.Event.OnConnectionOpened<*> -> pStockList.forEach {
                    service.subscribe(SubscribeCommand(it))
                }
                is com.tinder.scarlet.WebSocket.Event.OnMessageReceived -> {
                    val stockUpdate = service.observeStock().receive()
                    sendBlocking(stockUpdate)
                }
                /** TODO find a better default case */
                else -> logger.e(
                    "${WebSocketServiceScarletImpl::class.simpleName}",
                    "",
                    RuntimeException("Something went wrong")
                ) // mutableLiveData.postValue(BaseState.StateError(R.string.error))
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
//            client.dispatcher().executorService().shutdown()
        }
    }

    override fun unSubscribeAll() {
        pStockList.forEach {
            service.unSubscribe(UnSubscribeCommand(it))
        }
    }
}
