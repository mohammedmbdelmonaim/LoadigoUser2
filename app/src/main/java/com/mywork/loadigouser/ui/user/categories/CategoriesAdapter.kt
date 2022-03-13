package com.mywork.loadigouser.ui.user.categories

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemCategoryBinding
import com.mywork.loadigouser.databinding.ItemServiceBinding
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.Category
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import javax.inject.Inject

class CategoriesAdapter @Inject constructor() : ListAdapter<Category, CategoriesAdapter.ServicesHolder>(
    Category.itemCallback
) {
    inner class ServicesHolder(val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mcCategory.setOnClickListener {
                clickListener.onClickCategory(adapterPosition)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemCategoryBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_category,
            parent,
            false
        )
        return ServicesHolder(view)
    }

    override fun onBindViewHolder(holder: ServicesHolder, position: Int) {
        val category: Category = getItem(position)
        holder.binding.category = category
        holder.binding.executePendingBindings()
    }

    private lateinit var clickListener: ClickListener

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onClickCategory(position: Int)
    }
}