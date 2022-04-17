package com.mywork.loadigouser.ui.user.profile.help

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.mywork.loadigouser.R
import com.mywork.loadigouser.databinding.ItemQuestionBinding
import com.mywork.loadigouser.model.remote.response.GetHelpModel
import java.util.Collections.rotate
import javax.inject.Inject


class QuestionsAdapter @Inject constructor() :
    ListAdapter<GetHelpModel, QuestionsAdapter.ViewHolder>(object :
        DiffUtil.ItemCallback<GetHelpModel>() {
        override fun areItemsTheSame(oldItem: GetHelpModel, newItem: GetHelpModel): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: GetHelpModel, newItem: GetHelpModel): Boolean =
            oldItem == newItem
    }) {

    private lateinit var context: Context
    private val rotationAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(
            context,
            R.anim.rotate
        )
    }
    private val reverseRotationAnimation: Animation by lazy {
        AnimationUtils.loadAnimation(context, R.anim.rotate_reverse)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        context = parent.context
       val binding = ItemQuestionBinding.inflate(
            LayoutInflater.from(context),
            parent,
            false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val helpModel = getItem(position)
        holder.bindHelpItem(helpModel)

        holder.apply {
            holder.view.answerTv.isVisible = helpModel.visible == true
            holder.view.vLine.isVisible = helpModel.visible == true
            holder.view.questionHeaderLayout.setOnClickListener {
                holder.view.answerTv.isVisible = !holder.view.answerTv.isVisible
                holder.view.vLine.isVisible = holder.view.answerTv.isVisible
                if (holder.view.answerTv.isVisible) {
                    holder.view.arrow.startAnimation(rotationAnimation)
                } else {
                    holder.view.arrow.startAnimation(reverseRotationAnimation)
                }
            }
        }
    }

    inner class ViewHolder(val view: ItemQuestionBinding) :
        RecyclerView.ViewHolder(view.root) {

        fun bindHelpItem(helpModel: GetHelpModel) {
            view.help = helpModel
            view.executePendingBindings()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}