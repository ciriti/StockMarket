package com.example.stockmarket.ui.stockprice

import com.example.stockmarket.core.BaseViewModel
import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

class StockPriceViewModel(
    mainDispatcher: CoroutineContext = Dispatchers.Main,
    private val workerDispatcher: CoroutineContext = Dispatchers.IO
) : BaseViewModel(mainDispatcher) {

}