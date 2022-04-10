package com.mywork.loadigouser.ui.user.payment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemCardBinding
import com.mywork.loadigouser.databinding.ItemCategoryBinding
import com.mywork.loadigouser.databinding.ItemServiceBinding
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.Category
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import javax.inject.Inject

class CardsAdapter @Inject constructor() : ListAdapter<Category, CardsAdapter.CardsHolder>(
    Category.itemCallback
) {
    inner class CardsHolder(val binding: ItemCardBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.rbCard.setOnCheckedChangeListener { compoundButton, b ->

            }

            binding.tvCardNumber.setOnClickListener {
                it.isSelected = true
            }

        }
    }

    override fun getItemCount(): Int {
        return 4
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CardsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemCardBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_card,
            parent,
            false
        )
        return CardsHolder(view)
    }

    override fun onBindViewHolder(holder: CardsHolder, position: Int) {
        holder.binding.tvCardNumber.isSelected = true
//        val card: Category = getItem(position)
//        holder.binding.card = card
//        holder.binding.executePendingBindings()
    }

    private lateinit var clickListener: ClickListener

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onClickCategory(position: Int)
    }
}