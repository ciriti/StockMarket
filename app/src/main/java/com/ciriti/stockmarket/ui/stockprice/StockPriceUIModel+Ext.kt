package com.ciriti.stockmarket.ui.stockprice

import androidx.annotation.StringRes
import arrow.core.Either
import com.ciriti.stockmarket.data.StockInfo
import com.ciriti.stockmarket.data.utils.check
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

sealed class BaseState {
    data class StateSuccess(val uiStockModel: StockInfoUi) : BaseState()
    data class StateError(@StringRes val errorMessage: Int) : BaseState()
}
