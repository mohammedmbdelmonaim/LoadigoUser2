package com.mywork.loadigouser.util


import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView

import androidx.constraintlayout.widget.ConstraintLayout

import androidx.databinding.BindingAdapter

import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.target.Target
import com.mywork.loadigouser.R
import com.mywork.loadigouser.util.extensions.loadSvg


object ViewBindingAdapter {

    @SuppressLint("CheckResult")
    @JvmStatic
    @BindingAdapter("bind_image", "image_type", "place_holder", requireAll = false)
    fun bindImage(
        image: AppCompatImageView,
        path: Any? = null,
        imageType: ImageType,
        placeHolderResource: Any? = null
    ) {
        path?.let {
            val imagePath = when (imageType) {
                ImageType.COUNTRY -> "https://www.countryflags.io/$path/shiny/64.png"
                ImageType.PURE_PATH -> path
                ImageType.CATEGORY -> {
//                    if (SharedCodeConfiguration.isDebug) {
//                        "${SharedCodeConfiguration.url}CMS2/$path"
//                    } else {
//                        "${SharedCodeConfiguration.url}$path"
//                    }
                }
            }

            if (imagePath.toString().contains(".svg")) {
                image.loadSvg(imagePath.toString())
            } else {
                Glide.with(image).load(imagePath).diskCacheStrategy(DiskCacheStrategy.ALL).apply {
                    if (placeHolderResource != null) {
                        val drawable = placeHolderResource as Drawable
                        // @SuppressLint("CheckResult")
                        placeholder(drawable)
                        error(drawable)
                    }
                    error(R.mipmap.ic_launcher)
                    placeholder(R.mipmap.ic_launcher)
                }.into(image)
            }
        }
    }


    @JvmStatic
    @BindingAdapter("cardBackground")
    fun changeBackground(view: ConstraintLayout, statusId: Int) {
        when (statusId) {
//            0 -> {
//                view.setBackgroundColor(view.context.resources.getColor(R.color.new_order))
//            }
//            1 -> {
//                view.setBackgroundColor(view.context.resources.getColor(R.color.accept_order))
//            }
//            2 -> {
//                view.setBackgroundColor(view.context.resources.getColor(R.color.finish_order))
//            }
        }
    }
}