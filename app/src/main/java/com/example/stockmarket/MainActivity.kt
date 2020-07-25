package com.example.stockmarket

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.stockmarket.core.init
import com.example.stockmarket.ui.stockprice.StockPriceFragment
import com.example.stockmarket.ui.stockprice.StockPriceViewModel
import org.koin.android.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val vm by viewModel<StockPriceViewModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        savedInstanceState ?: init(R.id.fragmentContainer, StockPriceFragment())
    }
}