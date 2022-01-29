package me.admund.nmn

import androidx.test.espresso.idling.CountingIdlingResource

object EspressoIdleResourceHandler {
    val countingIdlingResource = CountingIdlingResource("MAIN")

    fun increment() {
        countingIdlingResource.increment()
    }

    fun decrement() {
        if (!countingIdlingResource.isIdleNow) {
            countingIdlingResource.decrement()
        }
    }
}
