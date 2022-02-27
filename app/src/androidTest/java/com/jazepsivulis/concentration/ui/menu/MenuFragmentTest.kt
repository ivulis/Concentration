package com.jazepsivulis.concentration.ui.menu

import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.rule.BaristaRule
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.ui.MainActivity

import org.junit.Before
import org.junit.Rule
import org.junit.Test

class MenuFragmentTest {

    @get:Rule
    val activityRule = BaristaRule.create(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.launchActivity()
    }

    @Test
    fun menu_fragment_is_visible_on_activity_launch() {
        // Given the activity is launched
        // Then the menu fragment is displayed
        assertDisplayed(R.id.menu_layout)
    }

    @Test
    fun menu_fragment_navigates_to_easy_game() {
        // Given the activity is launched
        // When we are clicking the new easy game button
        clickOn(R.id.easy_game_button)

        // Then the game fragment is shown
        assertDisplayed(R.id.game_layout)
    }

    @Test
    fun menu_fragment_navigates_to_medium_game() {
        // Given the activity is launched
        // When we are clicking the new medium game button
        clickOn(R.id.medium_game_button)

        // Then the game fragment is shown
        assertDisplayed(R.id.game_layout)
    }

    @Test
    fun menu_fragment_navigates_to_hard_game() {
        // Given the activity is launched
        // When we are clicking the new hard game button
        clickOn(R.id.hard_game_button)

        // Then the game fragment is shown
        assertDisplayed(R.id.game_layout)
    }

    @Test
    fun menu_fragment_navigates_to_high_scores() {
        // Given the activity is launched
        // When we are clicking the high scores button
        clickOn(R.id.high_scores_button)

        // Then the high scores fragment is shown
        assertDisplayed(R.id.high_scores_layout)
    }
}
