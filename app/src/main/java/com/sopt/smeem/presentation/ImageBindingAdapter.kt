package com.sopt.smeem.presentation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy

class ImageBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imgUrl"])
        fun load(imageView: ImageView, imageUrl: String) {
            Glide.with(imageView.context)
                .load(imageUrl)
                .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                .into(imageView)
        }

        @JvmStatic
        @BindingAdapter("imgRes")
        fun imageLoad(imageView: ImageView, resId: Int) = imageView.setImageResource(resId)
    }
}