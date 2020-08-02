package com.example.stockmarket

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.matcher.BoundedMatcher
import kotlinx.coroutines.delay
import org.hamcrest.Description
import org.hamcrest.Matcher

@Throws(Exception::class)
suspend fun waitAndRetry(task: () -> Unit) {
    var res: TestRes.NotVerified = TestRes.NotVerified(RuntimeException("Not initialize condition!"))
    repeat(20) {
        when (val t = checkCondition(task)) {
            TestRes.Verified -> return
            is TestRes.NotVerified -> res = t
        }
        delay(250)
    }
    throw res.th
}

fun checkCondition(task: () -> Unit): TestRes {
    return try {
        task()
        TestRes.Verified
    } catch (th: Throwable) {
        TestRes.NotVerified(th)
    }
}

sealed class TestRes {
    object Verified : TestRes()
    data class NotVerified(val th: Throwable) : TestRes()
}

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