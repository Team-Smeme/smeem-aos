package com.sopt.smeem.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ItemMyBadgeBinding
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.presentation.mypage.MyBadgeWelcomeAdaptor.Companion.diffUtil

class MyBadgeExtraAdaptor :
    ListAdapter<Badge, MyBadgeExtraAdaptor.MyBadgeExtraAdaptorViewHolder>(diffUtil) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MyBadgeExtraAdaptorViewHolder {
        val binding = ItemMyBadgeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyBadgeExtraAdaptorViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyBadgeExtraAdaptorViewHolder, position: Int) {
        holder.onBind(currentList[position])
    }

    class MyBadgeExtraAdaptorViewHolder(private val binding: ItemMyBadgeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun onBind(data: Badge) {
            with(binding) {
                binding.badge = data
            }
        }
    }
}