package com.ciriti.stockmarket.ui.stockprice

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ciriti.stockmarket.* // ktlint-disable
import com.ciriti.stockmarket.data.RxSocketService
import com.ciriti.stockmarket.data.StockInfo
import com.ciriti.stockmarket.data.utils.Logger
import com.ciriti.stockmarket.data.utils.debugLogger
import com.tinder.scarlet.WebSocket.Event.OnConnectionOpened
import io.mockk.* // ktlint-disable
import io.mockk.impl.annotations.MockK
import io.reactivex.Flowable
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class StockPriceFragmentRxJavaTest {

    @Rule
    @JvmField
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val rxTestingSchedulerRule = RxTestingSchedulerRule()

    @MockK
    lateinit var service: RxSocketService

    @Before
    fun setup() {
        /** mocked variable */
        MockKAnnotations.init(this, relaxUnitFun = true)
        every { service.observeStock() } returns Flowable.just(
            StockInfo("Apple", 10.001),
            StockInfo("Apple", 10.004),
            StockInfo("Apple", 10.005),
            StockInfo("Apple", 10.009)
        )

        every { service.observeWebSocketEvent() } returns Flowable.just(
            OnConnectionOpened("")
        )
    }

    @Test
    fun `send one update and receive a correct UI State`() {

        val eventList = mutableListOf<BaseState>()

        val sut = StockPriceViewModelRxJava(
            service = service,
            errorHandler = { 1 },
            logger = Logger.debugLogger()
        )

        /** collect the events to the ui */
        sut.liveData.observeForever { eventList.add(it) }

        /** trigger the updates */
        sut.subscribeAll()

        /** check */
        eventList.size assertEquals 4
        (eventList[0] as? BaseState.StateSuccess)
            .assertNotNull()
            .uiStockModel.price assertEquals "10.00"
        (eventList[1] as? BaseState.StateSuccess)
            .assertNotNull()
            .uiStockModel.price assertEquals "10.00"
        (eventList[2] as? BaseState.StateSuccess)
            .assertNotNull()
            .uiStockModel.price assertEquals "10.01"
        (eventList[3] as? BaseState.StateSuccess)
            .assertNotNull()
            .uiStockModel.price assertEquals "10.01"
    }
}
