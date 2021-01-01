package com.ciriti.stockmarket.ui.stockprice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ciriti.stockmarket.R
import com.ciriti.stockmarket.data.StockService
import com.ciriti.stockmarket.data.SubscribeCommand
import com.ciriti.stockmarket.data.UnSubscribeCommand
import com.ciriti.stockmarket.data.stockList
import com.ciriti.stockmarket.utils.Logger
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
                                { ifRight ->
                                    println("ViewModel obj $ifRight")
                                    mutableLiveData.postValue(BaseState.StateSuccess(ifRight))
                                }
                            )
                        }
                        /** TODO find a better default case */
                        else -> mutableLiveData.postValue(BaseState.StateError(R.string.error))
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