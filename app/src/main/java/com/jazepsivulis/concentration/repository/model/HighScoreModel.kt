package com.jazepsivulis.concentration.repository.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.jazepsivulis.concentration.common.HIGH_SCORE_TABLE

@Entity(tableName = HIGH_SCORE_TABLE)
data class HighScoreModel(
    @PrimaryKey val id: Int,
    val date: String,
    val playingTime: String,
    val guesses: String
)
