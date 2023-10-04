package com.jazepsivulis.concentration.common

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.ui.GameViewModel

class GameViewModelFactory(
    private val repository: GameRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GameViewModel(repository) as T
    }
}
