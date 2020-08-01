package com.example.stockmarket

import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeoutOrNull
import org.junit.Test

class FlowTest {
    fun simple(): Flow<Int> = flow {
        for (i in 1..3) {
            delay(100)
            println("Emitting $i")
            emit(i)
        }
    }

    @Test
    fun `test1 `() = runBlocking<Unit> {
        withTimeoutOrNull(250) { // Timeout after 250ms
            simple().collect { value -> println(value) }
        }
        println("Done")
    }

    @Test
    fun `test 2`() = runBlocking<Unit> {
        (1..10000)
            .asFlow()
            .filter { it % 2 == 0 }
            .map { "num: $it" }
            .buffer(Channel.UNLIMITED)
            .collect {
            delay(800)
            println(it)
        }
        println("Done")
    }
}