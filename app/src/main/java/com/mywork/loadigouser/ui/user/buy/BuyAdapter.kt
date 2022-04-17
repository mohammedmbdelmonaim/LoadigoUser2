package com.mywork.loadigouser.ui.user.buy

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemBuyBinding
import com.mywork.loadigouser.databinding.ItemOrderHistoryBinding
import com.mywork.loadigouser.model.remote.response.user.Category
import javax.inject.Inject

class BuyAdapter @Inject constructor() : ListAdapter<Category, BuyAdapter.OrdersHolder>(
    Category.itemCallback
) {
    inner class OrdersHolder(val binding: ItemBuyBinding) :
        RecyclerView.ViewHolder(binding.root){
//            init {
//                binding.tvDeliverByAddress.setOnClickListener {
//                    it.isSelected = true
//                }
//                binding.tvPickupByAddress.setOnClickListener {
//                    it.isSelected = true
//                }
//            }
        }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OrdersHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemBuyBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_buy,
            parent,
            false
        )
        return OrdersHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersHolder, position: Int) {
//        val category: Category = getItem(position)
////        holder.binding.category = category
//        holder.binding.tvPickupByAddress.isSelected = true
//        holder.binding.tvDeliverByAddress.isSelected = true
//        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return 2
    }

    private lateinit var clickListener: ClickListener

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onClickCategory(position: Int)
    }
}