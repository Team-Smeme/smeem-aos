package com.sopt.smeem.domain.dto

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RetrievedBadgeDto(
    val name: String,
    val imageUrl: String,
) : Parcelable
