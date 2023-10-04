package com.jazepsivulis.concentration.fakes

import com.jazepsivulis.concentration.common.GameLevel
import com.jazepsivulis.concentration.common.toDateString
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.repository.model.GamePiece
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*

class GameRepositoryFake : GameRepository {
    private val _gameTimer = MutableSharedFlow<String>(replay = 1)
    private val _onTimeUp = MutableSharedFlow<Unit>(replay = 1)

    val fakeHighScores = MutableStateFlow<List<HighScoreModel>>(emptyList())

    override val currentScore get() = _gameTimer.replayCache.firstOrNull() ?: ""
    override val currentTime get() = Date().time.toDateString()
    override val highScores: Flow<List<HighScoreModel>> = fakeHighScores
    override val gameTimer = _gameTimer.asSharedFlow()
    override val onTimeUp = _onTimeUp.asSharedFlow()

    override fun insertHighScore(highScoreModel: HighScoreModel) { /* Ignored */ }
    override fun getGamePieces(gameLevel: GameLevel): List<GamePiece> {
        var id = 1
        return ('A'..(gameLevel.lastPiece)).map { GamePiece(id++, it) }
            .plus(('A'..(gameLevel.lastPiece)).map { GamePiece(id++, it) }).shuffled().shuffled()
    }

    override fun startTimer() { /* Ignored */ }

    override fun stopTimer() { /* Ignored */ }
}
