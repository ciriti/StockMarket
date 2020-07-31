package com.example.stockmarket

import kotlinx.coroutines.*

fun main() = runBlocking {

    launch {  }
    val deferreds: List<Deferred<Int>> = (1..3).map {
        async {
            delay(1000L * it)
            log("Loading $it")
            it
        }
    }
    val sum = deferreds.awaitAll().sum()
    log("$sum")
}

suspend fun loadData(): Int {
    log("loading...")
    delay(1000L)
    log("loaded!")
    return 42
}

fun log(message: Any?) {
    println("[${Thread.currentThread().name}] $message")
}