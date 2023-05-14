package com.sopt.smeem.presentation.auth.onboarding

import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityOnBoardingBinding
import com.sopt.smeem.presentation.BindingActivity

class OnBoardingActivity :
    BindingActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private val vm: OnBoardingVM by viewModels()

    override fun constructLayout() {
        super.constructLayout()
        setUpFragments()
    }

    override fun addListeners() {
        onNext()
    }

    override fun addObservers() {
        super.addObservers()
    }

    private fun setUpFragments() {
        binding.tvOnBoardingHeaderNo.text = "1"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_on_boarding, SettingGoalFragment())
            .commit()
    }

    private fun onNext() {
        binding.btnOnBoardingNext.setOnClickListener {
            if (vm.step == 0) {
                binding.tvOnBoardingHeaderNo.text = "2"
                binding.tvOnBoardingHeaderTitleStatic.text = resources.getText(R.string.on_boarding_encouraging_header_title)
                binding.tvOnBoardingHeaderDescriptionStatic.text = resources.getText(R.string.on_boarding_encouraging_header_description)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_on_boarding, SettingEncouragingFragment())
                    .commit()
                vm.goToNext()
            } else if (vm.step == 1) {
                binding.tvOnBoardingHeaderNo.text = "3"
                binding.tvOnBoardingHeaderTitleStatic.text = resources.getText(R.string.on_boarding_time_header_title)
                binding.tvOnBoardingHeaderDescriptionStatic.text = resources.getText(R.string.on_boarding_time_header_description)
                supportFragmentManager.beginTransaction()
                    .replace(R.id.fcv_on_boarding, SettingTimeFragment())
                    .commit()
                vm.goToNext()
            }
        }
    }
}