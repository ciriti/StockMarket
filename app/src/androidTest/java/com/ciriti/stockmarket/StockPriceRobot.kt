package com.ciriti.stockmarket

import android.app.Activity
import androidx.test.espresso.Espresso
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers
import androidx.test.espresso.matcher.ViewMatchers
import com.ciriti.stockmarket.ui.stockprice.uicomponent.StockAdapter
import org.hamcrest.CoreMatchers
import org.hamcrest.Matchers

class StockPriceRobot {

    suspend fun verifyPrice2(price: String, position: Int): StockPriceRobot = apply {
        waitAndRetry {
            Espresso.onView(ViewMatchers.withId(R.id.stock_list))
                .check(
                    ViewAssertions.matches(
                        matchAtPositionInRecyclerView(
                            position,
                            ViewMatchers.hasDescendant(
                                CoreMatchers.allOf(
                                    ViewMatchers.withId(R.id.price),
                                    ViewMatchers.withText(price)
                                )
                            )
                        )
                    )
                )
        }
    }

    suspend fun clickListItem(position: Int): StockPriceRobot = apply {
        waitAndRetry {
            Espresso.onView(ViewMatchers.withId(R.id.stock_list))
                .perform(
                    RecyclerViewActions.actionOnItemAtPosition<StockAdapter.ViewHolder>(
                        position,
                        ViewActions.click()
                    )
                )
        }
    }

    suspend fun checkToastContent(text: String, activity: Activity): StockPriceRobot = apply {
        Espresso.onView(
            ViewMatchers.withText(text)
        )
            .inRoot(RootMatchers.withDecorView(Matchers.not(Matchers.`is`(activity.window.decorView))))
            .check(ViewAssertions.matches(ViewMatchers.isDisplayed()))
    }

    suspend fun verifyPrice(price: String, position: Int): StockPriceRobot = apply {
        waitAndRetry {
            // First, scroll to the view holder using the isInTheMiddle matcher.
            Espresso
                .onView(ViewMatchers.withId(R.id.stock_list))
                .perform(RecyclerViewActions.scrollToPosition<StockAdapter.ViewHolder>(position))
            //                getApplicationContext().getResources().getString(R.string.middle);
            Espresso.onView(ViewMatchers.withText(price))
                .check(
                    ViewAssertions.matches(ViewMatchers.isDisplayed())
                )
        }
    }
}
