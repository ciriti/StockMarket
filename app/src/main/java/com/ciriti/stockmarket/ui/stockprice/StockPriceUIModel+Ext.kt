package com.ciriti.stockmarket.ui.stockprice

import androidx.annotation.StringRes
import arrow.core.Either
import arrow.core.Left
import arrow.core.Right
import com.ciriti.stockmarket.data.StockInfo
import java.math.RoundingMode

fun StockInfo.toUiModel(): Either<Throwable, StockInfoUi> = check {
    StockInfoUi(
        isin = isin,
        price = price.toBigDecimal().setScale(2, RoundingMode.HALF_UP).toString()
    )
}

data class StockInfoUi(
    val isin: String,
    var price: String
)

fun <R> check(block: () -> R) = try {
    Right(block())
} catch (t: Throwable) {
    Left(t)
}

sealed class BaseState {
    data class StateSuccess(val uiStockModel: StockInfoUi) : BaseState()
    data class StateError(@StringRes val errorMessage: Int) : BaseState()
}
