package com.jazepsivulis.concentration.ui.highscores

import com.adevinta.android.barista.assertion.BaristaVisibilityAssertions.assertDisplayed
import com.adevinta.android.barista.interaction.BaristaClickInteractions.clickOn
import com.adevinta.android.barista.interaction.BaristaSleepInteractions
import com.adevinta.android.barista.rule.BaristaRule
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.injection.fakeRepository
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import com.jazepsivulis.concentration.ui.MainActivity

import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class HighScoresFragmentTest {

    @get:Rule
    val activityRule = BaristaRule.create(MainActivity::class.java)

    @Before
    fun setUp() {
        activityRule.launchActivity()
        clickOn(R.id.high_scores_button)
    }

    @After
    fun after() {
        fakeRepository.fakeHighScores.value = emptyList()
    }

    @Test
    fun high_scores_shows_all_elements() {
        assertDisplayed(R.id.high_scores_list)
        assertDisplayed(R.id.go_back_button)
    }

    @Test
    fun high_scores_are_displayed_correctly() {
        // When there is one high score to display
        val highScoreModel = HighScoreModel(0, "27.02.2022 20:00:00", "00:20:00", "2")
        fakeRepository.fakeHighScores.value = listOf(highScoreModel)

        // ... and there is a delay for assertDisplayed
        BaristaSleepInteractions.sleep(200)

        // Then high score is shown on the screen
        assertDisplayed(highScoreModel.date)
        assertDisplayed("Playing time: " + highScoreModel.playingTime)
        assertDisplayed("Guesses: " + highScoreModel.guesses)
    }
}
