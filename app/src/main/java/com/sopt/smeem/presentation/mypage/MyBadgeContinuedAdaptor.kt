package com.sopt.smeem.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ItemMyBadgeBinding
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.presentation.mypage.MyBadgeWelcomeAdaptor.Companion.diffUtil

class MyBadgeContinuedAdaptor :
    ListAdapter<Badge, MyBadgeContinuedAdaptor.MyBadgeContinuedAdaptorViewHolder>(diffUtil) {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBadgeContinuedAdaptor.MyBadgeContinuedAdaptorViewHolder {
        val binding = ItemMyBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBadgeContinuedAdaptorViewHolder(binding)
    }

    override fun onBindViewHolder(
        holder: MyBadgeContinuedAdaptor.MyBadgeContinuedAdaptorViewHolder,
        position: Int
    ) {
        holder.onBind(currentList[position])
    }

    class MyBadgeContinuedAdaptorViewHolder(private val binding: ItemMyBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Badge) {
            with(binding) {
                binding.badge = data
            }
        }
    }
}