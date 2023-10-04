package com.jazepsivulis.concentration.injection

import com.jazepsivulis.concentration.ui.game.GameFragment
import com.jazepsivulis.concentration.ui.highscores.HighScoresFragment
import com.jazepsivulis.concentration.ui.menu.MenuFragment
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [InjectionModule::class])
interface InjectionComponent {
    fun inject(target: GameFragment)
    fun inject(target: HighScoresFragment)
    fun inject(target: MenuFragment)
}
