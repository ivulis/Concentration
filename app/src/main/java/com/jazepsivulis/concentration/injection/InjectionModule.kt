package com.jazepsivulis.concentration.injection

import android.content.Context
import androidx.room.Room
import com.jazepsivulis.concentration.common.HIGH_SCORE_DATABASE
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.repository.GameRepositoryImpl
import com.jazepsivulis.concentration.repository.cache.GameDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class InjectionModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideGameRepository(database: GameDatabase): GameRepository = GameRepositoryImpl(database.gameDao())

    @Provides
    @Singleton
    fun provideDataBase() = Room
        .databaseBuilder(context, GameDatabase::class.java, HIGH_SCORE_DATABASE)
        .fallbackToDestructiveMigration()
        .build()
}
