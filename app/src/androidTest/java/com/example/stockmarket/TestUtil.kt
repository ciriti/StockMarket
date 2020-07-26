package com.example.stockmarket

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import kotlinx.coroutines.delay
import org.hamcrest.Description
import org.hamcrest.Matcher

fun matchAtPositionInRecyclerView(position: Int, itemMatcher: Matcher<View>): Matcher<View> {
    return object : BoundedMatcher<View, RecyclerView>(RecyclerView::class.java) {
        override fun describeTo(description: Description) {
            description.appendText("has item at position $position: ")
            itemMatcher.describeTo(description)
        }

        override fun matchesSafely(view: RecyclerView): Boolean {
            val viewHolder = view.findViewHolderForAdapterPosition(position) ?: return false
            return itemMatcher.matches(viewHolder.itemView)
        }
    }
}

@Throws(Exception::class)
suspend fun waitAndRetry(type : String = "-", task: () -> Unit) {
    var counter = 20
    while (counter > 0){
        if(checkCondition(task)) return
        delay(250)
        counter--
    }
    throw RuntimeException("====> Timeout ${20*250/1000} seconds, the condition {$type} has not been met <====")
}

fun checkCondition(task: () -> Unit): Boolean {
    return try {
        task()
        true
    } catch (th: Throwable) {
        false
    }
}