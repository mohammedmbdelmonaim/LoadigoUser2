package com.mywork.loadigouser.ui.user.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemCategoryBinding
import com.mywork.loadigouser.databinding.ItemNotificationBinding
import com.mywork.loadigouser.databinding.ItemServiceBinding
import com.mywork.loadigouser.model.remote.response.user.CategoriesResponse
import com.mywork.loadigouser.model.remote.response.user.Category
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import javax.inject.Inject

class NotificationsAdapter @Inject constructor() : ListAdapter<Category, NotificationsAdapter.NotificationsHolder>(
    Category.itemCallback
) {
    inner class NotificationsHolder(val binding: ItemNotificationBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationsHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemNotificationBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_notification,
            parent,
            false
        )
        return NotificationsHolder(view)
    }

    override fun onBindViewHolder(holder: NotificationsHolder, position: Int) {
//        val category: Category = getItem(position)
//        holder.binding.category = category
//        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int {
        return 10
    }
}