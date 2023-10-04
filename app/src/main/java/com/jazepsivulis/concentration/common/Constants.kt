package com.jazepsivulis.concentration.common

const val HIGH_SCORE_TABLE = "high_score_table"
const val HIGH_SCORE_DATABASE = "high_score_database"
const val MAX_GAME_TIME = 1000 * 60 * 60L

enum class GameLevel(val columnCount: Int, val lastPiece: Char) {
    EASY(2,'B'),
    MEDIUM(4,'H'),
    HARD(4,'J')
}
