package com.sopt.smeem.presentation.mypage

import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.databinding.ActivityEditTrainingGoalBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EditTrainingGoalActivity :
    BindingActivity<ActivityEditTrainingGoalBinding>(R.layout.activity_edit_training_goal) {

    private var buttons: Map<TrainingGoalType, MaterialButton>? = null
    private val vm: EditTrainingVM by viewModels()

    override fun constructLayout() {
        TrainingGoalType.DEVELOP.id = binding.icMyPageTrainingButton1.id
        TrainingGoalType.HOBBY.id = binding.icMyPageTrainingButton2.id
        TrainingGoalType.APPLY.id = binding.icMyPageTrainingButton3.id
        TrainingGoalType.BUSINESS.id = binding.icMyPageTrainingButton4.id
        TrainingGoalType.EXAM.id = binding.icMyPageTrainingButton5.id
        TrainingGoalType.NONE.id = binding.icMyPageTrainingButton6.id

        buttons = mapOf(
            TrainingGoalType.DEVELOP to binding.icMyPageTrainingButton1,
            TrainingGoalType.HOBBY to binding.icMyPageTrainingButton2,
            TrainingGoalType.APPLY to binding.icMyPageTrainingButton3,
            TrainingGoalType.BUSINESS to binding.icMyPageTrainingButton4,
            TrainingGoalType.EXAM to binding.icMyPageTrainingButton5,
            TrainingGoalType.NONE to binding.icMyPageTrainingButton6
        )
    }

    override fun addListeners() {
        onTouchButtons()
        onNextChanged()
        onTouchBack()
    }

    private fun onTouchButtons() {
        buttons?.values?.forEach { button ->
            button.setOnClickListener {
                if (TrainingGoalType.NO_SELECTED != vm.selectedGoal.value) {
                    buttons!![vm.selectedGoal.value]?.switchOff() // 기존 off
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

        binding.btnMyPageTrainingGoal.setOnClickListener {
            vm.sendServer(
                onError = { e ->
                    Toast.makeText(this, e.errorCode.message, Toast.LENGTH_SHORT).show()
                }
            )
        }
    }

    private fun onNextChanged() {
        vm.selectedGoal.observe(
            this@EditTrainingGoalActivity
        ) {
            binding.btnMyPageTrainingGoal.isEnabled = (it != TrainingGoalType.NO_SELECTED)
        }
    }

    private fun onTouchBack() {
        binding.topbarMyPageTraining.setNavigationOnClickListener {
            /* onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {
                 override fun handleOnBackPressed() {
                     goBack()
                 }
             })*/
            goBack()
            finish()
        }
    }

    private fun goBack() {
        super.onBackPressed()
    }
}