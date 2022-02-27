package com.jazepsivulis.concentration.ui.highscores

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.jazepsivulis.concentration.App
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.common.GameViewModelFactory
import com.jazepsivulis.concentration.common.launchUI
import com.jazepsivulis.concentration.common.openFragment
import com.jazepsivulis.concentration.databinding.FragmentHighScoresBinding
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.ui.GameViewModel
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class HighScoresFragment : Fragment() {

    @Inject
    lateinit var repository: GameRepository
    private lateinit var binding: FragmentHighScoresBinding

    private val viewModel by activityViewModels<GameViewModel> {
        GameViewModelFactory(repository)
    }

    private val adapter by lazy {
        HighScoresAdapter()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.component.inject(this)
        binding = FragmentHighScoresBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            highScoresList.adapter = adapter

            goBackButton.setOnClickListener {
                openFragment(R.id.navigation_menu)
            }
        }

        launchUI {
            viewModel.highScores.collect { newList ->
                adapter.highScores = newList
            }
        }
    }
}
