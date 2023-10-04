package com.jazepsivulis.concentration.repository.cache

import androidx.room.Database
import androidx.room.RoomDatabase
import com.jazepsivulis.concentration.repository.model.HighScoreModel

@Database(entities = [HighScoreModel::class], version = 1, exportSchema = false)
abstract class GameDatabase : RoomDatabase() {
    abstract fun gameDao(): GameDao
}
