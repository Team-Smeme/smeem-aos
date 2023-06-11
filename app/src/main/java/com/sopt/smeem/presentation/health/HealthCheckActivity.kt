package com.sopt.smeem.presentation.health

import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityHealthCheckBinding
import com.sopt.smeem.presentation.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HealthCheckActivity :
    BindingActivity<ActivityHealthCheckBinding>(R.layout.activity_health_check) {
    private val healthViewModel: ViewModel by viewModels()

    override fun constructLayout() {
        super.constructLayout()
        setUpHealth()
    }

    override fun addListeners() {
        super.addListeners()
        binding.btnHealthCheck.setOnClickListener {
            healthViewModel.connect(
                onError = { t -> Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show() }
            )
        }
    }

    override fun addObservers() {
        super.addObservers()
    }

    fun setUpHealth() {
        healthViewModel.result.observe(this) {
            binding.healthStatus = it
        }
    }
}