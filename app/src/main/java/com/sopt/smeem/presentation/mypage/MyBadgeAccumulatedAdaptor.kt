package com.sopt.smeem.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ItemMyBadgeBinding
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.presentation.mypage.MyBadgeWelcomeAdaptor.Companion.diffUtil

class MyBadgeAccumulatedAdaptor :
    ListAdapter<Badge, MyBadgeAccumulatedAdaptor.MyBadgeAccumulateAdaptorViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBadgeAccumulateAdaptorViewHolder {
        val binding = ItemMyBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBadgeAccumulateAdaptorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBadgeAccumulateAdaptorViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class MyBadgeAccumulateAdaptorViewHolder(private val binding: ItemMyBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Badge) {
            with(binding) {
                badge = data
            }
        }
    }
}