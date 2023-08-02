package com.sopt.smeem.presentation.calendar

import android.os.Bundle
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHomeBinding
import com.sopt.smeem.presentation.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeActivity : BindingActivity<ActivityHomeBinding>(R.layout.activity_home) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}
