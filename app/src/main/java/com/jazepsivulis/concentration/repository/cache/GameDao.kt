package com.jazepsivulis.concentration.repository.cache

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.jazepsivulis.concentration.common.HIGH_SCORE_TABLE
import com.jazepsivulis.concentration.repository.model.HighScoreModel
import kotlinx.coroutines.flow.Flow

@Dao
interface GameDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertHighScore(highScore: HighScoreModel)

    @Query("SELECT * FROM $HIGH_SCORE_TABLE")
    fun getHighScores(): Flow<List<HighScoreModel>>
}
