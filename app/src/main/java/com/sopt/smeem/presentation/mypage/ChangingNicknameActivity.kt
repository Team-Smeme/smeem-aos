package com.sopt.smeem.presentation.mypage

import android.view.View
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityChangingNicknameBinding
import com.sopt.smeem.presentation.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChangingNicknameActivity :
    BindingActivity<ActivityChangingNicknameBinding>(R.layout.activity_changing_nickname) {
    private val vm: ChangingNicknameVM by viewModels()


    override fun addListeners() {
        onTextWrite()
        onCompleted()
        onTouchBack()
    }

    private fun onTextWrite() {
        binding.etChangeNickname.addTextChangedListener { watcher ->
            if (watcher!!.length <= 0 || watcher!!.length > 16) {
                nextButtonOff()
            } else {
                nextButtonOn()
            }
        }
    }

    private fun onTouchBack() {
        binding.topbarChangeNickname.setNavigationOnClickListener {
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

    private fun onCompleted() {
        binding.btnChangeNicknameNext.setOnClickListener {
            if (nicknameDuplicated()) {
                binding.tvChangeNicknameDuplicated.visibility = View.VISIBLE
                binding.btnChangeNicknameNext.isEnabled = false
            } else {
                vm.send(binding.etChangeNickname.text.toString())
                goBack()
                if (!isFinishing) finish()
            }
        }
    }

    private fun nicknameDuplicated() = false // TODO : nickname 중복 api call


    private fun nextButtonOff() {
        binding.btnChangeNicknameNext.setBackgroundColor(
            resources.getColor(
                R.color.point_inactive,
                null
            )
        )
        binding.btnChangeNicknameNext.isEnabled = false
    }

    private fun nextButtonOn() {
        binding.btnChangeNicknameNext.setBackgroundColor(resources.getColor(R.color.point, null))
        binding.btnChangeNicknameNext.isEnabled = true
    }
}

class ChangingNicknameVM : ViewModel() {
    fun send(name: String): Unit //  TODO : nickname 변경 api call
    {
        viewModelScope.launch { sendServer(name) }
    }

    private suspend fun sendServer(name: String): Unit { // TODo

    }
}