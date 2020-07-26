package com.example.stockmarket

import androidx.test.espresso.Espresso
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.matcher.ViewMatchers
import org.hamcrest.CoreMatchers

class StockPriceRobot {

    suspend fun verifyPrice(price: String, position: Int): StockPriceRobot = apply {
        waitAndRetry("check $price at pos $position") {
            Espresso.onView(ViewMatchers.withId(R.id.stock_list))
                .check(
                    ViewAssertions.matches(
                        matchAtPositionInRecyclerView(
                            position, ViewMatchers.hasDescendant(
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
}
