package com.example.stockmarket.ui.stockprice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.data.StockService
import com.example.stockmarket.data.SubscribeCommand
import com.example.stockmarket.data.UnSubscribeCommand
import com.example.stockmarket.utils.Logger
import com.example.stockmarket.utils.stockList
import com.tinder.scarlet.WebSocket
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.reactive.consumeEach
import kotlin.coroutines.CoroutineContext

class StockPriceViewModel(
    private val service: StockService,
    private val errorHandler: (Throwable) -> Int,
    private val logger: Logger,
    private val pStockList : List<String> = stockList,
    private val workerDispatcher: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    private val mutableLiveData by lazy { MutableLiveData<BaseState>() }
    val liveData: LiveData<BaseState> get() = mutableLiveData

    fun subscribeAll() {
        viewModelScope.launch {
            launch(workerDispatcher) {
                service.observeWebSocketEvent().consumeEach { event ->
                    when(event){
                        is WebSocket.Event.OnConnectionOpened<*> -> pStockList.forEach {
                            service.subscribe(SubscribeCommand(it))
                        }
                        is WebSocket.Event.OnMessageReceived -> {
                            val stockUpdate = service.observeStock().receive()
                            stockUpdate.toUiModel().fold(
                                { throwable ->
                                    logger.e("${StockPriceViewModel::class.simpleName}", "", throwable)
                                    /**
                                     * process the exception type using the errorHandler fun
                                     * and return a value to send the UI
                                     */
                                    mutableLiveData.postValue(BaseState.StateError(errorHandler(throwable)))
                                },
                                { ifRight -> mutableLiveData.postValue(BaseState.StateSuccess(ifRight)) }
                            )
                        }
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