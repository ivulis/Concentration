package com.jazepsivulis.concentration.ui.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jazepsivulis.concentration.App
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.common.GameLevel
import com.jazepsivulis.concentration.common.GameViewModelFactory
import com.jazepsivulis.concentration.common.openFragment
import com.jazepsivulis.concentration.databinding.FragmentMenuBinding
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.ui.GameViewModel
import javax.inject.Inject

class MenuFragment : Fragment() {

    @Inject
    lateinit var repository: GameRepository
    private lateinit var binding: FragmentMenuBinding

    private val viewModel by activityViewModels<GameViewModel> {
        GameViewModelFactory(repository)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.component.inject(this)
        binding = FragmentMenuBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            easyGameButton.setOnClickListener {
                viewModel.updateGameLevel(GameLevel.EASY)
                openFragment(R.id.navigation_game)
            }
            mediumGameButton.setOnClickListener {
                viewModel.updateGameLevel(GameLevel.MEDIUM)
                openFragment(R.id.navigation_game)
            }
            hardGameButton.setOnClickListener {
                viewModel.updateGameLevel(GameLevel.HARD)
                openFragment(R.id.navigation_game)
            }
            highScoresButton.setOnClickListener {
                openFragment(R.id.navigation_scores)
            }
        }
    }
}
