package com.ciriti.stockmarket

import arrow.core.Either
import com.ciriti.stockmarket.data.StockInfo
import com.ciriti.stockmarket.ui.stockprice.StockInfoUi
import com.ciriti.stockmarket.ui.stockprice.toUiModel
import org.junit.Test

class StockPriceUIModelExtTest {

    @Test
    fun `StockInfo is converted to the UI model successfully`() {
        (StockInfo("apple", 10.00).toUiModel() as Either.Right<StockInfoUi>).run {
            b.price assertEquals "10.00"
        }
    }

    @Test
    fun `The price gets the correct round up`() {
        (StockInfo("apple", 10.009).toUiModel() as Either.Right<StockInfoUi>).run {
            b.price assertEquals "10.01"
        }
    }

}