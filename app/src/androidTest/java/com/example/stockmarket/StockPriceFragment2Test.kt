package com.example.stockmarket

import android.content.Intent
import androidx.test.espresso.NoMatchingViewException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.data.WebSocketService
import com.example.stockmarket.ui.stockprice.StockInfoUi
import com.example.stockmarket.ui.stockprice.StockPriceViewModelOkHttp
import com.example.stockmarket.utils.Logger
import com.example.stockmarket.utils.debugLogger
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module
import org.koin.test.KoinTest


@RunWith(AndroidJUnit4::class)
class StockPriceFragment2Test : KoinTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivity2::class.java, false, false)

    private val channel by lazy { Channel<StockInfo>() }

    @MockK
    lateinit var mockService: WebSocketService

    // Koin mock module, only the StockPriceViewModel
    val mockModule = module(override = true) {
        // viewmodel
        viewModel(override = true) {
            StockPriceViewModelOkHttp(
                service = mockService,//WebSocketServiceStub(channel.receiveAsFlow()),
                logger = Logger.debugLogger(),
                errorHandler = { R.string.error }
            )
        }
    }


    @Before
    fun setup() {
        /** mocked variable */
        MockKAnnotations.init(this, relaxUnitFun = true)
        coEvery { mockService.flowFrom() } returns channel.receiveAsFlow()
    }

    @Test
    fun first_element_has_the_smallest_rank_number() = runBlocking<Unit> {
        withTimeout(2000) {
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

    @Test(expected = RuntimeException::class)
    fun apple_price_is_wrong_and_this_test_fails() = runBlocking<Unit> {
        // 1. replace the module
        loadKoinModules(mockModule)
        // 2. start activity
        activityRule.launchActivity(Intent())

        StockPriceRobot()
            .also { channel.send(StockInfo("Apple", 10.001)) }
            .verifyPrice("20.00 €", 0)
    }

    @Test
    fun scrollToItem_click_check_toast() = runBlocking<Unit> {
        // 1. replace the module
        loadKoinModules(mockModule)
        // 2. start activity
        activityRule.launchActivity(Intent())

        StockPriceRobot()
            .clickListItem(9)
            .checkToastContent(StockInfoUi("Alphabet A", "-").toString(), activityRule.activity)
    }

    @Test(expected = NoMatchingViewException::class)
    fun scrollToItem_click_check_toast_will_fail() = runBlocking<Unit> {
        // 1. replace the module
        loadKoinModules(mockModule)
        // 2. start activity
        activityRule.launchActivity(Intent())

        StockPriceRobot()
            .clickListItem(9)
            .checkToastContent("hkjgasdfl", activityRule.activity)
    }

}