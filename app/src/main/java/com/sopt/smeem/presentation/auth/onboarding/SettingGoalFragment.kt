package com.sopt.smeem.presentation.auth.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.databinding.FragmentSettingGoalBinding
import com.sopt.smeem.presentation.BindingFragment
import com.sopt.smeem.presentation.auth.onboarding.GoalSelection.*

class SettingGoalFragment :
    BindingFragment<FragmentSettingGoalBinding>(R.layout.fragment_setting_goal) {
    private var buttons: Map<GoalSelection, MaterialButton>? = null
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        }).get(OnBoardingVM::class.java)
    }

    override fun constructLayout() {
        SELF_IMPROVEMENT.id = binding.icOnBoardingGoalButton1.id
        HOBBY.id = binding.icOnBoardingGoalButton2.id
        NATIVE.id = binding.icOnBoardingGoalButton3.id
        EXAM.id = binding.icOnBoardingGoalButton4.id
        READ.id = binding.icOnBoardingGoalButton5.id
        NO_IDEA.id = binding.icOnBoardingGoalButton6.id

        buttons = mapOf(
            SELF_IMPROVEMENT to binding.icOnBoardingGoalButton1,
            HOBBY to binding.icOnBoardingGoalButton2,
            NATIVE to binding.icOnBoardingGoalButton3,
            EXAM to binding.icOnBoardingGoalButton4,
            READ to binding.icOnBoardingGoalButton5,
            NO_IDEA to binding.icOnBoardingGoalButton6
        )
    }

    override fun addListeners() {
        onTouchButtons()
    }

    private fun onTouchButtons() {
        buttons?.values?.forEach { button ->
            button.setOnClickListener {
                if (vm.selectedGoal.value != NONE) {
                    buttons!![vm.selectedGoal.value]?.switchOff() // 기존 off
                    vm.upsert(GoalSelection.findById(button.id))

                    if (vm.selectedGoal.value!!.selected) {
                        button.switchOn() // 새로 On
                    } else {
                        vm.none()
                    }

                } else {
                    button.switchOn()
                    vm.upsert(GoalSelection.findById(button.id))
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        buttons = null
    }
}

internal fun MaterialButton.switchOn() {
    this.setIconResource(R.drawable.ic_selection_active)
    this.setTextColor(resources.getColor(R.color.point, null))
    this.setStrokeColorResource(R.color.point)
}

internal fun MaterialButton.switchOff() {
    this.setIconResource(R.drawable.ic_selection_inactive)
    this.setTextColor(resources.getColor(R.color.gray_600, null))
    this.setStrokeColorResource(R.color.gray_100)
}