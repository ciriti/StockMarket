package com.example.stockmarket

import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.data.WebSocketService
import kotlinx.coroutines.flow.Flow

class WebSocketServiceStub(private val flow: Flow<StockInfo>) :
    WebSocketService {
    override fun flowFrom(): Flow<StockInfo> = flow

    override fun unSubscribeAll() {}
}