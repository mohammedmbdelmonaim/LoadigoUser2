package com.mywork.loadigouser.ui.user.history

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemOrderHistoryBinding
import com.mywork.loadigouser.model.remote.response.user.Category
import javax.inject.Inject

class OrdersAdapter @Inject constructor() : ListAdapter<Category, OrdersAdapter.OrdersHolder>(
    Category.itemCallback
) {
    inner class OrdersHolder(val binding: ItemOrderHistoryBinding) :
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
        val view: ItemOrderHistoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_order_history,
            parent,
            false
        )
        return OrdersHolder(view)
    }

    override fun onBindViewHolder(holder: OrdersHolder, position: Int) {
        holder.binding.tvDeliverByAddress.text = "تعرف على اخبار مصر، شمال إفريقيا، تونس، الجزائر، ليبيا، المغرب"
        holder.binding.tvPickupByAddress.text = "تعرف على اخبار مصر، شمال إفريقيا، تونس، الجزائر، ليبيا، المغرب"
        holder.binding.tvDeliverByAddress.setOnClickListener {
            it.isSelected = true
        }
        holder.binding.tvPickupByAddress.setOnClickListener {
            it.isSelected = true
        }
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