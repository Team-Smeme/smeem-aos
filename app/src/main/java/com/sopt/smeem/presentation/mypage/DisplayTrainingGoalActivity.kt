package com.sopt.smeem.presentation.mypage

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.databinding.ActivityDisplayTrainingGoalBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DisplayTrainingGoalActivity : BindingActivity<ActivityDisplayTrainingGoalBinding>(R.layout.activity_display_training_goal) {
    private val viewModel by viewModels<EditTrainingVM>()

    override fun constructLayout() {
        getGoalData()
    }

    override fun addListeners() {
        goBack()
        goToMyPage()
    }

    override fun addObservers() {
        setGoalData()
    }

    private fun getGoalData() {
        viewModel.setSelectedGoal(intent.getSerializableExtra("selectedGoal") as TrainingGoalType)
        viewModel.getGoalDetail { e ->
            Toast.makeText(applicationContext, e.description(), Toast.LENGTH_SHORT).show()
        }
    }

    private fun goBack() {
        binding.tbMyPageDisplayTraining.setNavigationOnClickListener {
            finish()
        }
    }

    private fun goToMyPage() {
        binding.btnMyPageDisplayTraining.setOnSingleClickListener {
            viewModel.sendServer(
                onError = { e ->
                    Toast.makeText(this, e.errorCode.message, Toast.LENGTH_SHORT).show()
                }
            )
            Intent(this, MyPageActivity::class.java).apply {
                putExtra("snackbarText", resources.getString(R.string.my_page_edit_done_message))
                flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            }.run(::startActivity)
        }
    }

    private fun setGoalData() {
        viewModel.trainingGoal.observe(this) {
            binding.training = it
        }
    }
}