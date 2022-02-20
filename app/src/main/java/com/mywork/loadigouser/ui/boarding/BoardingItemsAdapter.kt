package io.paysky.yallasuperapp.ui.boarding

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.databinding.ItemBoardingBinding
import com.mywork.loadigouser.model.locale.BoardingItem

class BoardingItemsAdapter : RecyclerView.Adapter<BoardingItemsAdapter.BoardingItemViewHolder>() {
    var boardingItems: List<BoardingItem> = emptyList()
        set(value) {
            field = value
            notifyItemRangeChanged(0, this.itemCount)
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BoardingItemViewHolder {
        return BoardingItemViewHolder(
            ItemBoardingBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun onBindViewHolder(holder: BoardingItemViewHolder, position: Int) {
        holder.bindBoardingItem(boardingItems[position])
    }

    override fun getItemCount(): Int {
        return boardingItems.size
    }

    inner class BoardingItemViewHolder(private val binding: ItemBoardingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        internal fun bindBoardingItem(item: BoardingItem) {
            binding.boardingItem = item
            binding.executePendingBindings()
        }
    }
}