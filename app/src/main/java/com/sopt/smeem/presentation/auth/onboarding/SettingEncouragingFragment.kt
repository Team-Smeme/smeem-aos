package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingEncouragingBinding
import com.sopt.smeem.domain.model.auth.OnBoardingEncouraging
import com.sopt.smeem.presentation.BindingFragment

class SettingEncouragingFragment :
    BindingFragment<FragmentSettingEncouragingBinding>(R.layout.fragment_setting_encouraging) {
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        }).get(OnBoardingVM::class.java)
    }

    override fun constructLayout() {
        binding.onBoardingEncouraging = OnBoardingEncouraging(
            goal = vm.selectedGoal.value!!.title,
            howTo = vm.selectedGoal.value!!.howTo,
            goalDetail = vm.selectedGoal.value!!.goalDetail
        )
    }
}