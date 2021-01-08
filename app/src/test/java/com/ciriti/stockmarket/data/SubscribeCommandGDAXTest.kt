package com.ciriti.stockmarket.data

import com.ciriti.stockmarket.assertEquals
import org.junit.Test

class SubscribeCommandGDAXTest {

    @Test
    fun `GIVEN a list of gdax items CREATE the subscription item`() {
        val sut = SubscribeCommandGDAX(gdaxList).toString()
        sut.assertEquals(
            """
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": ["ETH-EUR", "ETH-USD", "ETH-USDC", "ETH-BTC", "BTC-EUR", "BTC-USDC", "ETH-BTC", "BTC-GBP"]
                }
              ]
            }
            """.trimIndent()
        )
    }

    @Test
    fun `GIVEN an UnSubscribeCommandGDAX VEFIRY the un-subscription item`() {
        val sut = UnSubscribeCommandGDAX().toString()
        sut.assertEquals(
            """
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": []
                }
              ]
            }
            """.trimIndent()
        )
    }
}
