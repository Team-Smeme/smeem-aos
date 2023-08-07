package com.sopt.smeem.presentation

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

class ImageBindingAdapter {

    companion object {
        @JvmStatic
        @BindingAdapter(value = ["imgUrl"])
        fun load(imageView: ImageView, imageUrl: String?) {
            imageUrl?.let(imageView::load)
        }

        @JvmStatic
        @BindingAdapter("imgRes")
        fun imageLoad(imageView: ImageView, resId: Int) = imageView.setImageResource(resId)
    }
}