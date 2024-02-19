package com.sopt.smeem.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ItemMyBadgeBinding
import com.sopt.smeem.domain.model.Badge

class MyBadgeWelcomeAdaptor :
    ListAdapter<Badge, MyBadgeWelcomeAdaptor.MyBadgeWelcomeAdaptorViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBadgeWelcomeAdaptorViewHolder {
        val binding = ItemMyBadgeBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MyBadgeWelcomeAdaptorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBadgeWelcomeAdaptorViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class MyBadgeWelcomeAdaptorViewHolder(private val binding: ItemMyBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Badge) {
            with(binding) {
                binding.badge = data
            }
        }
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<Badge>() {
            override fun areItemsTheSame(oldItem: Badge, newItem: Badge): Boolean =
                oldItem.name == newItem.name

            override fun areContentsTheSame(oldItem: Badge, newItem: Badge): Boolean =
                oldItem == newItem
        }
    }
}