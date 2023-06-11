package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.StudyGoal
import com.sopt.smeem.StudyGoal.*
import com.sopt.smeem.databinding.FragmentSettingGoalBinding
import com.sopt.smeem.presentation.BindingFragment
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn

class SettingGoalFragment :
    BindingFragment<FragmentSettingGoalBinding>(R.layout.fragment_setting_goal) {
    private var buttons: Map<StudyGoal, MaterialButton>? = null
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        })[OnBoardingVM::class.java]
    }

    override fun constructLayout() {
        SELF_IMPROVEMENT.id = binding.icOnBoardingGoalButton1.id
        HOBBY.id = binding.icOnBoardingGoalButton2.id
        NATIVE.id = binding.icOnBoardingGoalButton3.id
        BUSINESS.id = binding.icOnBoardingGoalButton4.id
        READ_AND_WRITE.id = binding.icOnBoardingGoalButton5.id
        NO_IDEA.id = binding.icOnBoardingGoalButton6.id

        buttons = mapOf(
            SELF_IMPROVEMENT to binding.icOnBoardingGoalButton1,
            HOBBY to binding.icOnBoardingGoalButton2,
            NATIVE to binding.icOnBoardingGoalButton3,
            BUSINESS to binding.icOnBoardingGoalButton4,
            READ_AND_WRITE to binding.icOnBoardingGoalButton5,
            NO_IDEA to binding.icOnBoardingGoalButton6
        )
    }

    override fun addListeners() {
        onTouchButtons()
    }

    private fun onTouchButtons() {
        buttons?.values?.forEach { button ->
            button.setOnClickListener {
                if (NONE != vm.selectedGoal.value) {
                    buttons!![vm.selectedGoal.value]?.switchOff() // 기존 off
                    vm.upsert(StudyGoal.findById(button.id))

                    if (vm.selectedGoal.value!!.selected) {
                        button.switchOn() // 새로 On
                    } else {
                        vm.none()
                    }

                } else { // 최초 선택
                    button.switchOn()
                    vm.upsert(StudyGoal.findById(button.id))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        buttons = null
    }
}