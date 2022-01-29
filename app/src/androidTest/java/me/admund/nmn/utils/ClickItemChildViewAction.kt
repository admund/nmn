package me.admund.nmn.utils

import android.view.View
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.Matcher

fun clickChildViewWithId(id: Int): ViewAction {
    return object : ViewAction {
        override fun getConstraints(): Matcher<View>? = null

        override fun getDescription() = "Click on a child view with specified id."

        override fun perform(uiController: UiController?, view: View) {
            view.findViewById<View>(id).performClick()
        }
    }
}
