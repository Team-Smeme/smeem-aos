package com.sopt.smeem.presentation.onboarding

import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingEncouragingBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingEncouragingFragment :
    BindingFragment<FragmentSettingEncouragingBinding>(R.layout.fragment_setting_encouraging) {
    private val vm: OnBoardingVM by activityViewModels()
    override fun constructLayout() {
        vm.getGoalDetail { e ->
            Toast.makeText(context, e.description(), Toast.LENGTH_SHORT).show()
        }

        vm.trainingGoal.observe(this) {
            binding.trainingGoal = it
        }
    }
}