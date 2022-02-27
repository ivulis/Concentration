package com.jazepsivulis.concentration.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.common.GameLevel
import com.jazepsivulis.concentration.common.launchIO
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.repository.model.GamePiece
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class GameViewModel(private val repository: GameRepository) : ViewModel() {

    private val _highScores = MutableSharedFlow<List<HighScoreModel>>(replay = 1)
    private val _guessCount = MutableSharedFlow<Int>(replay = 1)
    private val _gameLevel = MutableSharedFlow<GameLevel>(replay = 1)
    private val _gamePieces = MutableSharedFlow<List<GamePiece>>(replay = 1)
    private val _flippedPieces = MutableSharedFlow<Int>(replay = 1)
    private val _firstFlippedPiece = MutableSharedFlow<GamePiece>(replay = 1)
    private val _secondFlippedPiece = MutableSharedFlow<GamePiece>(replay = 1)
    private val _onGameOver = MutableSharedFlow<String?>(replay = 1)
    private val _onError = MutableSharedFlow<Int>(replay = 1)

    val highScores = _highScores.asSharedFlow()
    val guessCount = _guessCount.asSharedFlow()
    val gameLevel = _gameLevel.asSharedFlow()
    val gamePieces = _gamePieces.asSharedFlow()
    val flippedPieces = _flippedPieces.asSharedFlow()
    val firstFlippedPiece = _firstFlippedPiece.asSharedFlow()
    val secondFlippedPiece = _secondFlippedPiece.asSharedFlow()
    val onGameOver = _onGameOver.asSharedFlow()
    val onError = _onError.asSharedFlow()
    val gameTimer: SharedFlow<String> = repository.gameTimer

    init {
        viewModelScope.launch {
            repository.highScores.collect { newList ->
                _highScores.tryEmit(newList)
            }
        }

        viewModelScope.launch {
            repository.onTimeUp.collect {
                onGameOver(false)
            }
        }
    }

    fun updateGameLevel(gameLevel: GameLevel) {
        _gameLevel.tryEmit(gameLevel)
    }

    fun startGame() {
        repository.startTimer()
        _onGameOver.tryEmit(null)
        _guessCount.tryEmit(0)
        _flippedPieces.tryEmit(0)
        _gamePieces.tryEmit(repository.getGamePieces(gameLevel.replayCache[0]))
    }

    fun openPiece(pieceId: Int) {
        val letters = _gamePieces.replayCache.last().map { it.copy() }
        val selectedPiece = letters.first { it.id == pieceId }


        if (selectedPiece.isFlipped) {
            _onError.tryEmit(R.string.card_flipped)
        } else {
            _flippedPieces.tryEmit(flippedPieces.replayCache[0] + 1)
            selectedPiece.isFlipped = true

            if (flippedPieces.replayCache[0] == 1) {
                _firstFlippedPiece.tryEmit(selectedPiece)
            } else if (flippedPieces.replayCache[0] == 2) {
                _secondFlippedPiece.tryEmit(selectedPiece)
                _guessCount.tryEmit(guessCount.replayCache[0] + 1)

                if(firstFlippedPiece.replayCache[0].value == secondFlippedPiece.replayCache[0].value
                    && firstFlippedPiece.replayCache[0].id != secondFlippedPiece.replayCache[0].id) {
                    letters.first { it.id == firstFlippedPiece.replayCache[0].id }.isMatched = true
                    letters.first { it.id == secondFlippedPiece.replayCache[0].id }.isMatched = true
                }
            } else if (flippedPieces.replayCache[0] == 3) {
                val firstLetter = letters.first { it.id == firstFlippedPiece.replayCache[0].id }
                val secondLetter = letters.first { it.id == secondFlippedPiece.replayCache[0].id }
                if (!firstLetter.isMatched && !secondLetter.isMatched) {
                    firstLetter.isFlipped = false
                    secondLetter.isFlipped = false
                }
                _flippedPieces.tryEmit(1)
                _firstFlippedPiece.tryEmit(selectedPiece)
            }
        }

        if (letters.all { it.isMatched }) {
            onGameOver(true)
        }
        _gamePieces.tryEmit(letters)
    }

    private fun onGameOver(isGameWon: Boolean) {
        repository.stopTimer()
        val score = repository.currentScore
        _onGameOver.tryEmit(score)

        if (isGameWon) {
            val id = _highScores.replayCache.lastOrNull()?.maxByOrNull { it.id }?.id?.plus(1) ?: 0
            launchIO {
                repository.insertHighScore(HighScoreModel(
                    id,
                    repository.currentTime,
                    repository.currentScore,
                    guessCount.replayCache[0].toString()))
            }
        }
    }
}
