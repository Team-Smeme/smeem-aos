package com.sopt.smeem.presentation

import androidx.recyclerview.widget.DiffUtil

interface ItemDiff<T> {
    fun getDiff() = object : DiffUtil.ItemCallback<T>() {
        override fun areItemsTheSame(
            oldItem: T & Any,
            newItem: T & Any
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: T & Any,
            newItem: T & Any
        ): Boolean = oldItem == newItem
    }
}