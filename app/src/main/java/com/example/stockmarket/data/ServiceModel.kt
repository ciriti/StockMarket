package com.example.stockmarket.data

data class StockInfo(
    val isin: String,
    val price: Double
)

data class GDAXInfo(
    val product_id: String,
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

val gdaxList = listOf(
    "ETH-EUR",
    "ETH-USD",
    "ETH-USDC",
    "ETH-BTC",
    "BTC-EUR",
    "BTC-USDC",
    "ETH-BTC",
    "BTC-GBP"
)

data class SubscribeCommandGDAX(val gdaxList: List<String>) {
    override fun toString(): String {
        return """
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": ${gdaxList.map { "\"$it\"" }}
                }
              ]
            }
        """.trimIndent()
    }
}

class UnSubscribeCommandGDAX() {
    override fun toString(): String {
        return """
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": []
                }
              ]
            }
        """.trimIndent()
    }
}
