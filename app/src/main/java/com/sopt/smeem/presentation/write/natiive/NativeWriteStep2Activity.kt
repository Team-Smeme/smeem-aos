package com.sopt.smeem.presentation.write.natiive

import android.text.method.ScrollingMovementMethod
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityNativeWriteStep2Binding
import com.sopt.smeem.presentation.BindingActivity


class NativeWriteStep2Activity :
    BindingActivity<ActivityNativeWriteStep2Binding>(R.layout.activity_native_write_step2) {

    override fun constructLayout() {
        with (binding) {
            tvNativeStep2NativeDiary.movementMethod = ScrollingMovementMethod()
            with (layoutNativeStep2BottomToolbar.cbRandomTopic) {
                isChecked = true
                isEnabled = false
            }
        }
    }
}