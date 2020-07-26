package com.example.stockmarket

import arrow.core.Either
import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.ui.stockprice.StockInfoUi
import com.example.stockmarket.ui.stockprice.toUiModel
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