package com.ciriti.stockmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.ciriti.stockmarket.core.init
import com.ciriti.stockmarket.ui.stockprice.StockPriceFragmentRxJava

class MainActivityRxJava : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: init(R.id.fragmentContainer, StockPriceFragmentRxJava())
    }
}