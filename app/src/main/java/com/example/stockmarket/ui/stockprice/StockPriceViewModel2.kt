package com.example.stockmarket.ui.stockprice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.stockmarket.data.WebSocketService
import com.example.stockmarket.utils.Logger
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class StockPriceViewModel2(
    private val service: WebSocketService,
    private val errorHandler: (Throwable) -> Int,
    private val logger: Logger,
    private val workerDispatcher: CoroutineContext = Dispatchers.IO
) : ViewModel() {

    private val mutableLiveData by lazy { MutableLiveData<BaseState>() }
    val liveData: LiveData<BaseState> get() = mutableLiveData

    fun subscribeAll() {
        viewModelScope.launch {
            launch(workerDispatcher) {

                service.flowFrom().collect {
                    val stockUpdate = it.toUiModel()
                    stockUpdate.fold(
                        { throwable ->
                            logger.e("${StockPriceViewModel2::class.simpleName}", "", throwable)
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
            }
        }
    }

    fun unSubscribeAll() {
        service.unSubscribeAll()
    }
}