package com.mywork.loadigouser.ui.user.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemServiceBinding
import com.mywork.loadigouser.model.remote.response.user.ServiceResponse
import javax.inject.Inject

class ServicesAdapter @Inject constructor() : ListAdapter<ServiceResponse, ServicesAdapter.ServicesHolder>(
    ServiceResponse.itemCallback
) {
    inner class ServicesHolder(val binding: ItemServiceBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.mcService.setOnClickListener {
                clickListener.onClickService(adapterPosition)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicesHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view: ItemServiceBinding = DataBindingUtil.inflate(
            layoutInflater,
            R.layout.item_service,
            parent,
            false
        )
        return ServicesHolder(view)
    }

    override fun onBindViewHolder(holder: ServicesHolder, position: Int) {
        val serviceResponse: ServiceResponse = getItem(position)
        holder.binding.service = serviceResponse
        holder.binding.executePendingBindings()
    }

    private lateinit var clickListener: ClickListener

    fun setClickListener(clickListener: ClickListener) {
        this.clickListener = clickListener
    }

    interface ClickListener {
        fun onClickService(position: Int)
    }
}