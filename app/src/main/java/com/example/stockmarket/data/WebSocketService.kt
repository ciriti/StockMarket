package com.example.stockmarket.data

import kotlinx.coroutines.flow.Flow

interface WebSocketService{
    fun flowFrom(): Flow<StockInfo>
    fun unSubscribeAll()
    companion object
}