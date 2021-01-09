package com.ciriti.stockmarket.utils

import android.util.Log

interface Logger {
    fun i(tag: String, mess: String)
    fun e(tag: String, mess: String, throwable: Throwable)
    fun v(tag: String, mess: String)
    fun d(tag: String, mess: String)

    companion object
}

fun Logger.Companion.debugLogger(): Logger = DebugLogger()
fun Logger.Companion.productionLogger(): Logger = ProductionLogger()

private class DebugLogger : Logger {
    override fun i(tag: String, mess: String) {
        Log.i(tag, mess)
    }

    override fun e(tag: String, mess: String, throwable: Throwable) {
        Log.e(tag, mess)
    }

    override fun v(tag: String, mess: String) {
        Log.v(tag, mess)
    }

    override fun d(tag: String, mess: String) {
        Log.d(tag, mess)
    }
}

private class ProductionLogger : Logger {
    override fun i(tag: String, mess: String) {
        /** your production logger */
    }

    override fun e(tag: String, mess: String, throwable: Throwable) {
        /** your production logger */
    }

    override fun v(tag: String, mess: String) {
        /** your production logger */
    }

    override fun d(tag: String, mess: String) {
        /** your production logger */
    }
}

fun <T> T.printThreadName(text: String? = null) = apply {
    println("[Thread: ${Thread.currentThread().name}] $text")
}
fun <T> T.printThreadNameThis(text: String? = null) = apply {
    println("[Thread: ${Thread.currentThread().name}] [$this] $text")
}
