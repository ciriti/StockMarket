![https://github.com/ciriti/StockMarket/workflows/Test/badge.svg](https://github.com/ciriti/StockMarket/workflows/Test/badge.svg)
![https://github.com/ciriti/StockMarket/workflows/GitHub%20Tag%20and%20Release/badge.svg](https://github.com/ciriti/StockMarket/workflows/GitHub%20Tag%20and%20Release/badge.svg)
![https://github.com/ciriti/StockMarket/workflows/App%20Release/badge.svg](https://github.com/ciriti/StockMarket/workflows/App%20Release/badge.svg)
![https://api.bintray.com/packages/ciriti/gdax/okhttp-socket-ext/images/download.svg](https://api.bintray.com/packages/ciriti/gdax/okhttp-socket-ext/images/download.svg)

# StockMarket

##### Table of Contents  
- [Intro](#intro)  
- [Architecture](#architecture)  
- [Main modules](#main-modules)  
- [Intro](#intro)  
- [Intro](#intro)  
- [Used api](#used-api)  
- [Config](#config)  

## Intro

## Architecture

## Main modules

## App module

## Data-layer module

## Utility socket module

## CICD and quality

## Used api

## Config

The endpoint used in this example is below 

```
wss://ws-feed.gdax.com
```

You can test it using the following utility 

```
https://www.websocket.org/echo.html
```

### Request

```json
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

### Response

```json
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

## Config 

### Bintray 

Check that you have the `bintray` repository. 

```gradle
// Add Bintray to your repositories
repositories {
    maven { url  "https://dl.bintray.com/ciriti/gdax" }
}
```

### Dependencies

Pick one of your Koin dependency:

#### Gradle Plugin

```gradle
dependencies {
    implementation "io.github.ciriti:okhttp-socket-ext:1.3.0"
    implementation "io.github.ciriti:gdax-data:1.3.0"
}
```