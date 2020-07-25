package com.example.stockmarket.data

import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.channels.ReceiveChannel

interface StockService {
    @Send
    fun subscribe(command: SubscribeAction)

    @Send
    fun unSubscribe(command: UnSubscribe)

    @Receive
    fun observeStock(): ReceiveChannel<StockInfo>

    @Receive
    fun observeWebSocketEvent(): Stream<WebSocket.Event>
}


interface Command{
    val stockId : String
}

data class StockInfo(
    val isin: String,
    val price: Double
)

data class SubscribeAction(
    val subscribe: String
)
data class UnSubscribe(
    val subscribe: String
)