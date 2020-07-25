package com.example.stockmarket.ui.stockprice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.data.StockService
import com.example.stockmarket.data.SubscribeCommand
import com.example.stockmarket.data.UnSubscribeCommand
import com.example.stockmarket.utils.stockList
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.consumeEach
import kotlin.coroutines.CoroutineContext

class StockPriceViewModel(
    private val service: StockService,
    private val errorHandler: (Throwable) -> Int,
    private val pStockList : List<String> = stockList,
    private val workerDispatcher: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    private val mutableLiveData by lazy { MutableLiveData<BaseState>() }
    val liveData: LiveData<BaseState> get() = mutableLiveData

    fun subscribe(command: SubscribeCommand) {
        viewModelScope.launch {
            launch(workerDispatcher) { service.subscribe(command) }
        }
    }

    fun unSubscribe(command: UnSubscribeCommand) {
        viewModelScope.launch {
            launch(workerDispatcher) { service.unSubscribe(command) }
        }
    }

    fun observeStock() {

    }

    fun subscribeAll() {
        viewModelScope.launch {

            service.observeWebSocketEvent().consumeEach { event ->
                when(event){
                    is WebSocket.Event.OnConnectionOpened<*> -> pStockList.forEach {
                        service.subscribe(SubscribeCommand(it))
                    }
                    is WebSocket.Event.OnConnectionFailed -> { }
                    is WebSocket.Event.OnConnectionClosing -> {}
                    is WebSocket.Event.OnConnectionClosed -> {}
                    is WebSocket.Event.OnMessageReceived -> {
                        val stockUpdate = service.observeStock().receive()
                        stockUpdate.toUiModel().fold(
                            { ifLeft ->
                                /** process the exception type return a value to send the UI */
                                val errorId = errorHandler(ifLeft)
                                mutableLiveData.postValue(BaseState.StateError(errorId))
                            },
                            { ifRight -> mutableLiveData.postValue(BaseState.StateSuccess(ifRight())) }
                        )
                    }

                }
            }

        }
    }

    fun unSubscribeAll(){
        pStockList.forEach {
            service.unSubscribe(UnSubscribeCommand(it))
        }
    }


}