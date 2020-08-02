package com.example.stockmarket

import android.content.Intent
import androidx.test.espresso.NoMatchingViewException
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.rule.ActivityTestRule
import com.example.stockmarket.data.RxSocketService
import com.example.stockmarket.data.StockInfo
import com.example.stockmarket.data.WebSocketService
import com.example.stockmarket.ui.stockprice.StockInfoUi
import com.example.stockmarket.ui.stockprice.StockPriceViewModel2
import com.example.stockmarket.ui.stockprice.StockPriceViewModelRxJava
import com.example.stockmarket.utils.Logger
import com.example.stockmarket.utils.debugLogger
import com.tinder.scarlet.WebSocket
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.reactivex.Flowable
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
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
import java.util.concurrent.TimeUnit


@RunWith(AndroidJUnit4::class)
class StockPriceFragmentRxJavaTest : KoinTest {

    @get:Rule
    val activityRule = ActivityTestRule(MainActivityRxJava::class.java, false, false)

    @MockK
    lateinit var service: RxSocketService

    // Koin mock module, only the StockPriceViewModel
    val mockModule = module(override = true) {
        // viewmodel
        viewModel(override = true) {
            StockPriceViewModelRxJava(
                service = service,
                logger = Logger.debugLogger(),
                errorHandler = { R.string.error }
            )
        }
    }


    @Before
    fun setup() {
        /** mocked variable */
        MockKAnnotations.init(this, relaxUnitFun = true)

        every { service.observeStock() } returns Flowable.just(
            StockInfo("Apple", 10.001),
            StockInfo("Tesla", 11.001),
            StockInfo("Xiaomi", 12.001),
            StockInfo("Amazon", 13.001),
            StockInfo("Spotify", 14.001),
            StockInfo("Samsung", 15.001),
            StockInfo("Core Dax", 16.001)
        ).doOnEach { Thread.sleep(500) }

        every { service.observeWebSocketEvent() } returns Flowable.just(
            WebSocket.Event.OnConnectionOpened("")
        )
    }

    @Test
    fun first_element_has_the_smallest_rank_number() = runBlocking<Unit> {
            // 1. replace the module
            loadKoinModules(mockModule)
            // 2. start activity
            activityRule.launchActivity(Intent())

            StockPriceRobot()
                .verifyPrice("10.00 €", 0)
                .verifyPrice("11.00 €", 1)
                .verifyPrice("12.00 €", 2)
                .verifyPrice("13.00 €", 3)
                .verifyPrice("14.00 €", 4)
                .verifyPrice("15.00 €", 5)
                .verifyPrice("16.00 €", 6)

    }

    @Test(expected = RuntimeException::class)
    fun apple_price_is_wrong_and_this_test_fails() = runBlocking<Unit> {
        // 1. replace the module
        loadKoinModules(mockModule)
        // 2. start activity
        activityRule.launchActivity(Intent())

        StockPriceRobot()
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