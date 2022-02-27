package com.jazepsivulis.concentration.ui

import app.cash.turbine.test
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.common.GameLevel
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.repository.model.GamePiece
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.RelaxedMockK
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*

import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.powermock.modules.junit4.PowerMockRunner

@Suppress("EXPERIMENTAL_API_USAGE")
@RunWith(PowerMockRunner::class)
class GameViewModelTest {

    @RelaxedMockK
    private lateinit var repository: GameRepository
    private lateinit var viewModel: GameViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        Dispatchers.setMain(Dispatchers.Default)
        viewModel = GameViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `updateGameLevel() should update gameLevel flow`() = runTest {
        // Given user chosen game level
        val gameLevel = GameLevel.EASY

        // When updateGameLevel() is called
        viewModel.updateGameLevel(gameLevel)

        // Then gameLevel flow is updated
        viewModel.gameLevel.test {
            assertEquals(GameLevel.EASY, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `startGame() should start the timer and update the flows`() = runTest {
        // Given user chosen game level
        val gameLevel = GameLevel.EASY
        viewModel.updateGameLevel(gameLevel)

        // ... and a list of game pieces
        val mockGamePieces = listOf(GamePiece(0, 'A'), GamePiece(1, 'B'))
        every { repository.getGamePieces(gameLevel) } returns mockGamePieces

        // When the game is started
        viewModel.startGame()

        // Then the timer is started
        verify(exactly = 1) { repository.startTimer() }

        // ... and flows are updated
        viewModel.onGameOver.test {
            assertEquals(null, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.guessCount.test {
            assertEquals(0, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.flippedPieces.test {
            assertEquals(0, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.gamePieces.test {
            assertEquals(mockGamePieces, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `openPiece() should update flippedPieces, gamePieces flows on one flipped piece`() = runTest {
        // Given a new game
        val mockGamePieces = startGame()

        // When openPiece() is called
        viewModel.openPiece(1)

        // Then the following flows are updated
        viewModel.flippedPieces.test {
            assertEquals(1, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.firstFlippedPiece.test {
            assertEquals(1, viewModel.firstFlippedPiece.replayCache[0].id)
            cancelAndConsumeRemainingEvents()
        }
        mockGamePieces.first().run {
            isFlipped = true
        }
        viewModel.gamePieces.test {
            assertEquals(mockGamePieces, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `openPiece() should update flippedPieces, gamePieces flows on two matched pieces`() = runTest {
        // Given a new game
        val mockGamePieces = startGame()

        // When openPiece() is called
        viewModel.openPiece(1)
        viewModel.openPiece(2)

        // Then the following flows are updated
        viewModel.flippedPieces.test {
            assertEquals(2, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.firstFlippedPiece.test {
            assertEquals(1, viewModel.firstFlippedPiece.replayCache[0].id)
            cancelAndConsumeRemainingEvents()
        }
        viewModel.secondFlippedPiece.test {
            assertEquals(2, viewModel.secondFlippedPiece.replayCache[0].id)
            cancelAndConsumeRemainingEvents()
        }
        mockGamePieces[0].run {
            isFlipped = true
            isMatched = true
        }
        mockGamePieces[1].run {
            isFlipped = true
            isMatched = true
        }
        viewModel.gamePieces.test {
            assertEquals(mockGamePieces, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `openPiece() should update flippedPieces, gamePieces flows on two unmatched pieces`() = runTest {
        // Given a new game
        val mockGamePieces = startGame()

        // When openPiece() is called
        viewModel.openPiece(1)
        viewModel.openPiece(3)

        // Then the following flows are updated
        viewModel.flippedPieces.test {
            assertEquals(2, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        viewModel.firstFlippedPiece.test {
            assertEquals(1, viewModel.firstFlippedPiece.replayCache[0].id)
            cancelAndConsumeRemainingEvents()
        }
        viewModel.secondFlippedPiece.test {
            assertEquals(3, viewModel.secondFlippedPiece.replayCache[0].id)
            cancelAndConsumeRemainingEvents()
        }
        mockGamePieces[0].run {
            isFlipped = true
            isMatched = false
        }
        mockGamePieces[2].run {
            isFlipped = true
            isMatched = false
        }
        viewModel.gamePieces.test {
            assertEquals(mockGamePieces, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `openPiece() should emit error if same piece is clicked twice`() = runTest {
        // Given a new game
        startGame()

        // When openPiece() is called two times on same piece
        viewModel.openPiece(1)
        viewModel.openPiece(1)

        // Then error is emitted
        assertEquals(R.string.card_flipped, viewModel.onError.replayCache[0])
    }

    @Test
    fun `openPiece() should insert high score if game is won`() = runTest {
        // Given a new game
        val mockPlayingTime = "00:10:000"
        val mockGameTime = "27.02.2022 20:22:20"
        val mockGuessCount = "2"
        every { repository.currentScore } returns mockPlayingTime
        every { repository.currentTime } returns mockGameTime
        val mockGamePieces = startGame()

        // When openPiece() is called
        viewModel.openPiece(1)
        viewModel.openPiece(2)
        viewModel.openPiece(3)
        viewModel.openPiece(4)

        // Then the following flows are updated
        viewModel.onGameOver.test {
            assertEquals(mockPlayingTime, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        mockGamePieces.forEach { piece ->
            piece.isFlipped = true
            piece.isMatched = true
        }
        viewModel.gamePieces.test {
            assertEquals(mockGamePieces, awaitItem())
            cancelAndConsumeRemainingEvents()
        }
        verify { repository.insertHighScore(HighScoreModel(0, mockGameTime, mockPlayingTime, mockGuessCount)) }
    }

    private fun startGame(): List<GamePiece> {
        val gameLevel = GameLevel.EASY
        viewModel.updateGameLevel(gameLevel)
        val mockGamePieces = listOf(
            GamePiece(1, 'A'),
            GamePiece(2, 'A'),
            GamePiece(3, 'B'),
            GamePiece(4, 'B')
        )
        every { repository.getGamePieces(viewModel.gameLevel.replayCache[0]) } returns mockGamePieces
        viewModel.startGame()
        return mockGamePieces
    }
}
