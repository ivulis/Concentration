package com.jazepsivulis.concentration.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.jazepsivulis.concentration.databinding.ItemGamePieceBinding
import com.jazepsivulis.concentration.repository.model.GamePiece
import kotlin.properties.Delegates

class GameAdapter(
    private val onGamePieceClicked: (gamePiece: GamePiece) -> Unit
) : RecyclerView.Adapter<GameAdapter.ViewHolder>() {
    // Rule - after passing an immutable list, never edit what is inside of it, for DiffUtil to work properly
    var gamePieces: List<GamePiece> by Delegates.observable(emptyList()) { _, old, new ->
        DiffUtil.calculateDiff(DifferenceUtil(old, new)).dispatchUpdatesTo(this)
    }

    inner class ViewHolder(val binding: ItemGamePieceBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = ViewHolder(
        ItemGamePieceBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = gamePieces[position]
        holder.binding.item = item
        holder.binding.gamePiece.setOnClickListener {
            onGamePieceClicked(item)
        }
    }

    override fun getItemCount() = gamePieces.size

    inner class DifferenceUtil(
        private val old: List<GamePiece>,
        private val new: List<GamePiece>,
    ) : DiffUtil.Callback() {
        override fun getOldListSize() = old.size

        override fun getNewListSize() = new.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition].id == new[newItemPosition].id

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int) =
            old[oldItemPosition] == new[newItemPosition]
    }
}
