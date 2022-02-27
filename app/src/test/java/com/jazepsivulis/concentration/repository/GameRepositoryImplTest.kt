package com.jazepsivulis.concentration.repository

import com.jazepsivulis.concentration.common.GameLevel
import com.jazepsivulis.concentration.repository.cache.GameDao
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import io.mockk.MockKAnnotations
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@RunWith(PowerMockRunner::class)
class GameRepositoryImplTest {

    @RelaxedMockK
    private lateinit var gameDao: GameDao
    private lateinit var repository: GameRepositoryImpl

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        repository = GameRepositoryImpl(gameDao)
    }

    @Test
    fun `insertHighScore should update the database`() {
        // Given some mock high score
        val mockHighScore = HighScoreModel(0, "0", "0", "1")
        // When insertHighScore() is called
        repository.insertHighScore(mockHighScore)
        // Then the DB function was called with correct data
        verify { gameDao.insertHighScore(mockHighScore) }
    }

    @Test
    fun `getGamePieces should return list of GamePiece with correct amount of pieces`() {
        // Given that chosen game level is medium
        val gameLevel = GameLevel.MEDIUM
        // when getGamePieces() is called
        val list = repository.getGamePieces(gameLevel)
        // Then it returns GamePiece list with correct amount of pieces
        assertEquals(16, list.size)
    }
}
