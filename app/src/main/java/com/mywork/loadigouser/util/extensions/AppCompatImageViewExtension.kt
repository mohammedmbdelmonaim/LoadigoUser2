package com.mywork.loadigouser.util.extensions

import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import coil.ImageLoader
import coil.decode.SvgDecoder
import coil.request.ImageRequest
import coil.size.Scale
import com.mywork.loadigouser.R


fun AppCompatImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .crossfade(200)
        .error(R.mipmap.ic_launcher)
        .placeholder(R.mipmap.ic_launcher)
        .scale(Scale.FIT)
        .data(url)
        .size(this.context.resources.getDimension(R.dimen._55sdp).toInt() ,
            this.context.resources.getDimension(R.dimen._55sdp).toInt() )
        .target(this)
        .build()

    imageLoader.enqueue(request)
}
fun ImageView.loadSvg(url: String) {
    val imageLoader = ImageLoader.Builder(this.context)
        .componentRegistry { add(SvgDecoder(this@loadSvg.context)) }
        .build()

    val request = ImageRequest.Builder(this.context)
        .crossfade(true)
        .scale(Scale.FIT)
        .crossfade(200)
        .size(this.context.resources.getDimension(R.dimen._55sdp).toInt() ,
            this.context.resources.getDimension(R.dimen._55sdp).toInt() )
        .data(url)
        .error(R.mipmap.ic_launcher)
        .placeholder(R.mipmap.ic_launcher)
        .target(this)
        .build()

    imageLoader.enqueue(request)
}