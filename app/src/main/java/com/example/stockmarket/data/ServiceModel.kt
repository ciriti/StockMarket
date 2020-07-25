package com.example.stockmarket.data

data class StockInfo(
    val isin: String,
    val price: Double
)

data class SubscribeCommand(
    val subscribe: String
)
data class UnSubscribeCommand(
    val unsubscribe: String
)