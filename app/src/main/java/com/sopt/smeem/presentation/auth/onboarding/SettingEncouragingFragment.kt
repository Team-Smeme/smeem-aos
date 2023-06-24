package com.sopt.smeem.presentation.auth.onboarding

import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingEncouragingBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingFragment

class SettingEncouragingFragment :
    BindingFragment<FragmentSettingEncouragingBinding>(R.layout.fragment_setting_encouraging) {
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        })[OnBoardingVM::class.java]
    }

    override fun constructLayout() {
        vm.getGoalDetail { e ->
            Toast.makeText(context, e.description(), Toast.LENGTH_SHORT).show()
        }

        vm.trainingGoal.observe(this) {
            binding.trainingGoal = it
        }
    }
}