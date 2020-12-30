![Test](https://github.com/ciriti/StockMarket/workflows/Test/badge.svg)
![GitHub Tag and Release](https://github.com/ciriti/StockMarket/workflows/GitHub%20Tag%20and%20Release/badge.svg)

# StockMarket

```
https://www.websocket.org/echo.html

wss://ws-feed.gdax.com
```
Request
```
{
  "type": "subscribe",
  "channels": [
    {
      "name": "ticker",
      "product_ids": [
        "BTC-EUR",
        "ETH-EUR"
      ]
    }
  ]
}
```
Response:
```
{
  "type": "ticker",
  "sequence": 9537693195,
  "product_id": "BTC-EUR",
  "price": "22950",
  "open_24h": "21589.61",
  "volume_24h": "2592.29258956",
  "low_24h": "21430.64",
  "high_24h": "23300",
  "volume_30d": "69254.40965118",
  "best_bid": "22942.96",
  "best_ask": "22950.00",
  "side": "buy",
  "time": "2020-12-30T16:59:26.344287Z",
  "trade_id": 32667801,
  "last_size": "0.0083387"
}
```