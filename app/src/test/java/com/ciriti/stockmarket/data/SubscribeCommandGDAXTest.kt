package com.ciriti.stockmarket.data

import com.ciriti.stockmarket.assertEquals
import org.junit.Test

class SubscribeCommandGDAXTest{

    @Test
    fun `GIVEN a list of gdax items CREATE the subscription item`(){
        val sut = SubscribeCommandGDAX(gdaxList).toString()
        sut.assertEquals("""
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": ["BTC-EUR", "ETH-EUR"]
                }
              ]
            }
        """.trimIndent())
    }

    @Test
    fun `GIVEN an UnSubscribeCommandGDAX VEFIRY the un-subscription item`(){
        val sut = UnSubscribeCommandGDAX().toString()
        sut.assertEquals("""
            {
              "type": "subscribe",
              "channels": [
                {
                  "name": "ticker",
                  "product_ids": []
                }
              ]
            }
        """.trimIndent())
    }
}