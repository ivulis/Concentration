package com.jazepsivulis.concentration.repository.model

import androidx.room.PrimaryKey

data class GamePiece(
    @PrimaryKey val id: Int,
    val value: Char,
    var isFlipped: Boolean = false,
    var isMatched: Boolean = false
) {
    fun valueString() = value.toString()
}
