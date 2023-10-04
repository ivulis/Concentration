package com.jazepsivulis.concentration.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.jazepsivulis.concentration.App
import com.jazepsivulis.concentration.R
import com.jazepsivulis.concentration.common.GameViewModelFactory
import com.jazepsivulis.concentration.common.launchUI
import com.jazepsivulis.concentration.common.openFragment
import com.jazepsivulis.concentration.databinding.FragmentGameBinding
import com.jazepsivulis.concentration.repository.GameRepository
import com.jazepsivulis.concentration.ui.GameViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

class GameFragment : Fragment() {

    @Inject
    lateinit var repository: GameRepository

    private lateinit var binding: FragmentGameBinding

    private val viewModel by activityViewModels<GameViewModel> {
        GameViewModelFactory(repository)
    }

    private val gameAdapter by lazy {
        GameAdapter { piece ->
            viewModel.openPiece(piece.id)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        App.component.inject(this)
        binding = FragmentGameBinding.inflate(inflater, container, false)
        binding.gameGridView.layoutManager = GridLayoutManager(
            requireContext(),
            viewModel.gameLevel.replayCache[0].columnCount
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.run {
            endGameButton.setOnClickListener {
                openFragment(R.id.navigation_menu)
            }
            gameGridView.adapter = gameAdapter

            lifecycleScope.launch {
                viewModel.onError.collect { errorStringResource ->
                    Snackbar.make(binding.root, getString(errorStringResource), Snackbar.LENGTH_LONG).show()
                }
            }
        }
        setupCollectors()
        viewModel.startGame()
    }

    private fun setupCollectors() {
        launchUI {
            viewModel.gamePieces.collect { pieces ->
                gameAdapter.gamePieces = pieces
            }
        }
        launchUI {
            viewModel.guessCount.collect { guessCount ->
                binding.guessesLabel.text = getString(R.string.guess_template, guessCount)
            }
        }
        launchUI {
            viewModel.onGameOver.collect { time ->
                if (time == null) return@collect
                context?.let {
                    AlertDialog.Builder(it)
                        .setMessage(getString(R.string.game_over, time, viewModel.guessCount.replayCache[0]))
                        .setPositiveButton("OK") { popup, _ ->
                            popup.dismiss()
                            openFragment(R.id.navigation_menu)
                        }
                        .setCancelable(false)
                        .show()
                }
            }
        }
        launchUI {
            viewModel.gameTimer.collect { time ->
                binding.gameTimerTextview.text = time
            }
        }
    }
}
