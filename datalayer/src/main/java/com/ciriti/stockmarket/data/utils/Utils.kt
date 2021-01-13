package com.ciriti.stockmarket.data.utils

import arrow.core.Left
import arrow.core.Right

fun <R> check(block: () -> R) = try {
    Right(block())
} catch (t: Throwable) {
    Left(t)
}
