package com.ciriti.stockmarket

import com.ciriti.stockmarket.data.StockInfo
import com.ciriti.stockmarket.data.WebSocketService
import kotlinx.coroutines.flow.Flow

class WebSocketServiceStub(private val flow: Flow<StockInfo>) :
    WebSocketService {
    override fun flowFrom(): Flow<StockInfo> = flow

    override fun unSubscribeAll() {}
}
