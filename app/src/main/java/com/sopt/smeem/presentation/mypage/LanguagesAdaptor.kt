package com.sopt.smeem.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.sopt.smeem.databinding.ItemMyPageLanguageBinding
import com.sopt.smeem.domain.model.Language
import com.sopt.smeem.presentation.ItemDiff

class LanguagesAdaptor :
    ListAdapter<Language, LanguagesAdaptor.LanguageVH>(LanguageDiff.getDiff()) {

    class LanguageVH(private val binding: ItemMyPageLanguageBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun onBind(data: Language) {
            with(binding) {
                binding.language = data
            }
        }
    }

    override fun getItemCount() = 1 // one default now

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = LanguageVH(
        ItemMyPageLanguageBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
    )

    override fun onBindViewHolder(holder: LanguageVH, position: Int) {
        holder.onBind(getItem(position) as Language)
    }
}

internal object LanguageDiff : ItemDiff<Language>