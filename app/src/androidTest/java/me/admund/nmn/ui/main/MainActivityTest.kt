package me.admund.nmn.ui.main

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.IdlingRegistry
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import androidx.test.rule.ActivityTestRule
import me.admund.nmn.EspressoIdleResourceHandler
import me.admund.nmn.R
import me.admund.nmn.utils.clickChildViewWithId
import org.hamcrest.Matchers.not
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    val activityTestRule = ActivityTestRule(MainActivity::class.java)

    @Before
    fun registerIdle() {
        IdlingRegistry.getInstance().register(EspressoIdleResourceHandler.countingIdlingResource)
    }

    @After
    fun unregisterIdle() {
        IdlingRegistry.getInstance().unregister(EspressoIdleResourceHandler.countingIdlingResource)
    }

    @Test
    fun test_if_not_favorite_country_is_visible_after_checking_showOnlyFavorite_when_no_country_is_favorite() {
        val countriesRecyclerView = onView(withId(R.id.countries_recycler_view))
        countriesRecyclerView.check(matches(hasDescendant(withText(TEST_COUNTRY_NAME))))

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())

        countriesRecyclerView.check(matches(hasDescendant(withText(TEST_COUNTRY_NAME))))

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())
    }

    @Test
    fun test_if_not_favorite_country_is_visible_after_checking_showOnlyFavorite_when_other_country_is_favorite() {
        val countriesRecyclerView = onView(withId(R.id.countries_recycler_view))
        countriesRecyclerView.check(matches(hasDescendant(withText(TEST_COUNTRY_NAME))))
        countriesRecyclerView.perform(
            actionOnItemAtPosition<CountryViewHolder>(
                1,
                clickChildViewWithId(R.id.favorite_image_view)
            )
        )

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())

        countriesRecyclerView.check(matches(not(hasDescendant(withText(TEST_COUNTRY_NAME)))))

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())

        countriesRecyclerView.perform(
            actionOnItemAtPosition<CountryViewHolder>(
                1,
                clickChildViewWithId(R.id.favorite_image_view)
            )
        )
    }

    @Test
    fun test_if_favorite_country_is_visible_after_checking_showOnlyFavorite() {
        val countriesRecyclerView = onView(withId(R.id.countries_recycler_view))
        countriesRecyclerView.check(matches(hasDescendant(withText(TEST_COUNTRY_NAME))))
        countriesRecyclerView.perform(
            actionOnItemAtPosition<CountryViewHolder>(
                2,
                clickChildViewWithId(R.id.favorite_image_view)
            )
        )

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())

        countriesRecyclerView.check(matches(hasDescendant(withText(TEST_COUNTRY_NAME))))

        onView(withId(R.id.show_favorites_only_check_box)).perform(click())

        countriesRecyclerView.perform(
            actionOnItemAtPosition<CountryViewHolder>(
                2,
                clickChildViewWithId(R.id.favorite_image_view)
            )
        )
    }

    companion object {
        const val TEST_COUNTRY_NAME = "Algeria"
    }
}
