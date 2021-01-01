package com.ciriti.stockmarket.data

import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface StockService {
    @Send
    fun subscribe(command: SubscribeCommand)

    @Send
    fun unSubscribe(command: UnSubscribeCommand)

    @Receive
    fun observeStock(): ReceiveChannel<StockInfo>

    @Receive
    fun observeWebSocketEvent(): Stream<WebSocket.Event>
}