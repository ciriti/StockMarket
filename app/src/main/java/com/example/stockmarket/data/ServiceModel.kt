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

val stockList = listOf(
    "Apple",
    "Tesla",
    "Xiaomi",
    "Amazon",
    "Spotify",
    "Samsung",
    "Core Dax",
    "Netflix",
    "Master Card",
    "Alphabet A"
)