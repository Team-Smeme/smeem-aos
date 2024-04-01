package com.sopt.smeem.presentation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.imageLoader
import coil.load
import coil.request.ImageRequest
import com.sopt.smeem.R

class ImageBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imgUrl"])
        fun load(imageView: ImageView, imageUrl: String?) {
            imageView.load(imageUrl) {
                crossfade(true)
                placeholder(R.drawable.img_badge_placeholder)
            }
        }

        @JvmStatic
        @BindingAdapter("imageUrlPreload")
        fun ImageView.preloadImage(url: String?) {
            url?.let {
                val request = ImageRequest.Builder(context)
                    .data(it)
                    .build()

                context.imageLoader.enqueue(request)
            }
        }

        @JvmStatic
        @BindingAdapter("imgRes")
        fun imageLoad(imageView: ImageView, resId: Int) = imageView.setImageResource(resId)
    }
}
