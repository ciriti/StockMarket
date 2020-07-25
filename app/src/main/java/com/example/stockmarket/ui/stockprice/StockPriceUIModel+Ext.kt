package com.example.stockmarket.ui.stockprice

import androidx.annotation.StringRes
import arrow.core.Left
import arrow.core.Right
import com.example.stockmarket.data.StockInfo
import java.math.BigDecimal
import java.math.RoundingMode

fun StockInfo.toUiModel() {
    check<Throwable, StockInfoUi> {
        StockInfoUi(
            isin = isin,
            price = price.toBigDecimal().setScale(2, RoundingMode.HALF_UP)
        )
    }
}

data class StockInfoUi(
    val isin: String,
    val price: BigDecimal
)

fun <L, R> check(block: () -> R) = try {
    Right(block)
} catch (t: Throwable) {
    Left(t)
}

sealed class BaseState {
    data class StateSuccess(val uiStockModel : StockInfoUi) : BaseState()
    data class StateError(@StringRes val errorMessage: Int) : BaseState()
}