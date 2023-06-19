package com.sopt.smeem.presentation.write.foreign

import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityForeignWriteBinding
import com.sopt.smeem.presentation.BindingActivity

class ForeignWriteActivity :
    BindingActivity<ActivityForeignWriteBinding>(R.layout.activity_foreign_write) {

    private val viewModel by viewModels<ForeignWriteViewModel>()

    override fun constructLayout() {
        with(binding) {
            etForeignWrite.requestFocus()
            vm = viewModel
            lifecycleOwner = this@ForeignWriteActivity
        }
    }

}