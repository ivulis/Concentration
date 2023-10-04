package com.jazepsivulis.concentration.ui.game

import androidx.recyclerview.widget.RecyclerView
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.NoMatchingViewException
import androidx.test.espresso.ViewInteraction
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.RootMatchers.isDialog
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.rule.BaristaRule
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.ui.MainActivity
import org.junit.Before
import org.junit.Rule
import org.junit.Test


class GameFragmentTest {

    @get:Rule
    val activityRule = BaristaRule.create(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.launchActivity()
        clickOn(R.id.easy_game_button)
    }

    @Test
    fun game_fragment_elements_are_visible() {
        assertDisplayed(R.id.game_grid_view)
        assertDisplayed(R.id.guesses_label)
        assertDisplayed(R.id.end_game_button)
    }

    @Test
    fun winning_game_shows_dialog_that_navigates_back_to_menu() {
        // Given seeing the game fragment
        // When all card pairs are matched
        while (!onView(withText("OK")).isDisplayed()) {
            val position = (0..3).random()
            onView(withId(R.id.game_grid_view))
                .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(position, click()))
        }

        // Then the game over dialog is displayed
        onView(withText("OK"))
            .inRoot(isDialog())
            .check(matches(isDisplayed()))

        // ... and when clicking on the OK
        clickOn("OK")

        // ... then you are navigated back to the menu
        assertDisplayed(R.id.menu_layout)
    }

    @Test
    fun clicking_on_same_piece_twice_shows_error_message() {
        // Given seeing the game fragment
        // When same piece is clicked twice
        onView(withId(R.id.game_grid_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))
        onView(withId(R.id.game_grid_view))
            .perform(RecyclerViewActions.actionOnItemAtPosition<RecyclerView.ViewHolder>(0, click()))

        // Then error message is displayed
        assertDisplayed(R.string.card_flipped)
    }

    private fun ViewInteraction.isDisplayed(): Boolean {
        return try {
            check(matches(ViewMatchers.isDisplayed()))
            true
        } catch (e: NoMatchingViewException) {
            false
        }
    }
}
