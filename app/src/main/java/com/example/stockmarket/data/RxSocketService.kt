package com.example.stockmarket.data

import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import io.reactivex.Flowable

interface RxSocketService {
    @Send
    fun subscribe(action: SubscribeCommand)

    @Send
    fun unSubscribe(action: UnSubscribeCommand)

    @Receive
    fun observeStock(): Flowable<StockInfo>

    @Receive
    fun observeWebSocketEvent(): Flowable<WebSocket.Event>
}