package com.sopt.smeem.presentation.write.foreign

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ForeignWriteViewModel @Inject constructor() : ViewModel() {
    val topic = MutableLiveData("")
    val diary = MutableLiveData("")

}