package com.jazepsivulis.concentration.repository

import android.os.CountDownTimer
import com.jazepsivulis.concentration.common.*
import com.jazepsivulis.concentration.repository.cache.GameDao
import com.jazepsivulis.concentration.repository.model.GamePiece
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import java.util.*

interface GameRepository {
    val highScores: Flow<List<HighScoreModel>>
    val gameTimer: SharedFlow<String>
    val onTimeUp: SharedFlow<Unit>
    val currentScore: String
    val currentTime: String
    fun insertHighScore(highScoreModel: HighScoreModel)
    fun getGamePieces(gameLevel: GameLevel): List<GamePiece>
    fun startTimer()
    fun stopTimer()
}

class GameRepositoryImpl(private val gameDao: GameDao) : GameRepository {

    private val _gameTimer = MutableSharedFlow<String>(replay = 1)
    private val _onTimeUp = MutableSharedFlow<Unit>(replay = 1)

    private var timer = object : CountDownTimer(MAX_GAME_TIME, 10) {
        override fun onTick(elapsedTime: Long) {
            val date = Date(MAX_GAME_TIME - elapsedTime)
            _gameTimer.tryEmit(date.time.toTimeString())
        }

        override fun onFinish() {
            _onTimeUp.tryEmit(Unit)
        }
    }

    override val currentScore get() = _gameTimer.replayCache.firstOrNull() ?: ""
    override val currentTime get() = Date().time.toDateString()
    override val highScores = gameDao.getHighScores()
    override val gameTimer = _gameTimer.asSharedFlow()
    override val onTimeUp = _onTimeUp.asSharedFlow()

    override fun insertHighScore(highScoreModel: HighScoreModel) =
        gameDao.insertHighScore(highScoreModel)

    override fun getGamePieces(gameLevel: GameLevel): List<GamePiece> {
        var id = 1
        return ('A'..(gameLevel.lastPiece)).map { GamePiece(id++, it) }
            .plus(('A'..(gameLevel.lastPiece)).map { GamePiece(id++, it) }).shuffled().shuffled()
    }

    override fun startTimer(){
        timer.start()
    }

    override fun stopTimer() {
        timer.cancel()
    }
}
