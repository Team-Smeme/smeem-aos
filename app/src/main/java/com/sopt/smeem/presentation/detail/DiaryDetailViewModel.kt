package com.sopt.smeem.presentation.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map

class DiaryDetailViewModel : ViewModel() {
    val topic = MutableLiveData("")
    val diary = MutableLiveData("")
    val date = MutableLiveData("")
    val writer = MutableLiveData("")

    val isTopicExist: LiveData<Boolean> = topic.map { it != "" }
}