package com.sopt.smeem.presentation.onboarding

import androidx.fragment.app.activityViewModels
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.TrainingGoalType.APPLY
import com.sopt.smeem.TrainingGoalType.BUSINESS
import com.sopt.smeem.TrainingGoalType.DEVELOP
import com.sopt.smeem.TrainingGoalType.EXAM
import com.sopt.smeem.TrainingGoalType.HOBBY
import com.sopt.smeem.TrainingGoalType.NONE
import com.sopt.smeem.TrainingGoalType.NO_SELECTED
import com.sopt.smeem.databinding.FragmentSettingGoalBinding
import com.sopt.smeem.presentation.BindingFragment
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingGoalFragment :
    BindingFragment<FragmentSettingGoalBinding>(R.layout.fragment_setting_goal) {
    private var _buttons: Map<TrainingGoalType, MaterialButton>? = null
    private val buttons: Map<TrainingGoalType, MaterialButton>
        get() = _buttons ?: emptyMap()

    private val vm: OnBoardingVM by activityViewModels()
    override fun constructLayout() {
        DEVELOP.id = binding.icOnBoardingGoalButton1.id
        HOBBY.id = binding.icOnBoardingGoalButton2.id
        APPLY.id = binding.icOnBoardingGoalButton3.id
        BUSINESS.id = binding.icOnBoardingGoalButton4.id
        EXAM.id = binding.icOnBoardingGoalButton5.id
        NONE.id = binding.icOnBoardingGoalButton6.id

        _buttons = mapOf(
            DEVELOP to binding.icOnBoardingGoalButton1,
            HOBBY to binding.icOnBoardingGoalButton2,
            APPLY to binding.icOnBoardingGoalButton3,
            BUSINESS to binding.icOnBoardingGoalButton4,
            EXAM to binding.icOnBoardingGoalButton5,
            NONE to binding.icOnBoardingGoalButton6
        )

        buttonSelected()
    }

    override fun addListeners() {
        onTouchButtons()
    }

    private fun onTouchButtons() {
        buttons.values.forEach { button ->
            button.setOnSingleClickListener {
                if (NO_SELECTED != vm.selectedGoal.value) {
                    buttons[vm.selectedGoal.value]?.switchOff() // 기존 off
                    vm.upsert(TrainingGoalType.findById(button.id))

                    if (vm.selectedGoal.value!!.selected) {
                        button.switchOn() // 새로 On
                    } else {
                        vm.none()
                    }

                } else { // 최초 선택
                    button.switchOn()
                    vm.upsert(TrainingGoalType.findById(button.id))
                }
            }
        }
    }

    private fun buttonSelected() {
        if (vm.selectedGoal.value != NO_SELECTED) {
            buttons[vm.selectedGoal.value]?.switchOn()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _buttons = null
    }
}