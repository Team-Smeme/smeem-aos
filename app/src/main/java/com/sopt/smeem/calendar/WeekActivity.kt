package com.sopt.smeem.calendar

import android.os.Bundle
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityWeekBinding
import com.sopt.smeem.presentation.BindingActivity

class WeekActivity : BindingActivity<ActivityWeekBinding>(R.layout.activity_week) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }
}