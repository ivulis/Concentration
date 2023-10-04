package com.jazepsivulis.concentration.injection

import android.content.Context
import com.jazepsivulis.concentration.fakes.GameRepositoryFake
import com.jazepsivulis.concentration.repository.GameRepository
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

val fakeRepository = GameRepositoryFake()

@Module
class MockInjectionModule(private val context: Context) {
    @Provides
    @Singleton
    fun provideGameRepository(): GameRepository = fakeRepository
}
