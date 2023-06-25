package com.sopt.smeem.presentation.write.natiive

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NativeWriteStep1ViewModel: ViewModel() {
    val topic = MutableLiveData("")
    val diary = MutableLiveData("")
}