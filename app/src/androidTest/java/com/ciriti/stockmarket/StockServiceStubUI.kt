package com.ciriti.stockmarket

import com.ciriti.stockmarket.data.StockInfo
import com.ciriti.stockmarket.data.StockService
import com.ciriti.stockmarket.data.SubscribeCommand
import com.ciriti.stockmarket.data.UnSubscribeCommand
import com.tinder.scarlet.Message
import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.utils.FlowableStream
import io.reactivex.BackpressureStrategy
import io.reactivex.Observable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel

class StockServiceStubUI(
    private val channel: Channel<StockInfo>,
    private val eventList: List<WebSocket.Event>
) : StockService {

    companion object {
        val message = """
            {
              "isin": "Apple",
              "price": 102.04359020013433,
              "bid": 102.03359020013433,
              "ask": 102.05359020013434
            }
        """.trimIndent()
        val eventList = listOf<WebSocket.Event>(
            WebSocket.Event.OnConnectionOpened(""),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message)),
            WebSocket.Event.OnMessageReceived(Message.Text(message))
        )
        val errorEventList = listOf<WebSocket.Event>(
            WebSocket.Event.OnConnectionFailed(RuntimeException()),
            WebSocket.Event.OnMessageReceived(Message.Text(message))
        )
    }

    override fun subscribe(command: SubscribeCommand) {}

    override fun unSubscribe(command: UnSubscribeCommand) {}

    override fun observeStock(): ReceiveChannel<StockInfo> = channel

    override fun observeWebSocketEvent(): Stream<WebSocket.Event> {

        val o: Observable<WebSocket.Event> = Observable.fromIterable(eventList)
        return FlowableStream<WebSocket.Event>(o.toFlowable(BackpressureStrategy.BUFFER))
    }
}
