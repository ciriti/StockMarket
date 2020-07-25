package com.example.stockmarket.ui.stockprice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.stockmarket.core.BaseViewModel
import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.data.StockService
import com.example.stockmarket.data.SubscribeCommand
import com.example.stockmarket.data.UnSubscribeCommand
import com.tinder.scarlet.Stream
import com.tinder.scarlet.WebSocket
import com.tinder.scarlet.ws.Receive
import com.tinder.scarlet.ws.Send
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.ReceiveChannel
import kotlin.coroutines.CoroutineContext

class StockPriceViewModel(
    val service : StockService,
    mainDispatcher: CoroutineContext = Dispatchers.Main,
    private val workerDispatcher: CoroutineContext = Dispatchers.IO
) : BaseViewModel(mainDispatcher) {

    private val mutableLiveData by lazy { MutableLiveData<BaseState>() }
    val liveData: LiveData<BaseState> get() = mutableLiveData

    fun subscribe(command: SubscribeCommand){

    }

    fun unSubscribe(command: UnSubscribeCommand){

    }

    fun observeStock(){

    }

    fun observeWebSocketEvent(){

    }


}