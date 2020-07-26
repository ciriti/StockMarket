package com.example.stockmarket

import android.content.Intent
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.ui.stockprice.StockPriceViewModel
import com.example.stockmarket.utils.Logger
import com.example.stockmarket.utils.debugLogger
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

@RunWith(AndroidJUnit4::class)
class StockPriceFragmentTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity::class.java, false, false)

    private val channel by lazy { Channel<StockInfo>() }

    // Koin mock module, only the StockPriceViewModel
    val mockModule = module(override = true) {
        // viewmodel
        viewModel(override = true) {
            StockPriceViewModel(
                service = StockServiceStubUI(channel, StockServiceStubUI.eventList),
                logger = Logger.debugLogger(),
                errorHandler = { R.string.error }
            )
        }
    }

    @Test
    fun first_element_has_the_smallest_rank_number() = runBlocking<Unit> {
        // 1. replace the module
        loadKoinModules(mockModule)
        // 2. start activity
        activityRule.launchActivity(Intent())

        StockPriceRobot()
            .also { channel.send(StockInfo("Apple", 10.001)) }
            .verifyPrice("10.00 €", 0)
            .also { channel.send(StockInfo("Tesla", 11.001)) }
            .verifyPrice("11.00 €", 1)
            .also { channel.send(StockInfo("Xiaomi", 12.001)) }
            .verifyPrice("12.00 €", 2)
            .also { channel.send(StockInfo("Amazon", 13.001)) }
            .verifyPrice("13.00 €", 3)
            .also { channel.send(StockInfo("Spotify", 14.001)) }
            .verifyPrice("14.00 €", 4)
            .also { channel.send(StockInfo("Samsung", 15.001)) }
            .verifyPrice("15.00 €", 5)
            .also { channel.send(StockInfo("Core Dax", 16.001)) }
            .verifyPrice("16.00 €", 6)
    }

}