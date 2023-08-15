package com.sopt.smeem.presentation.join

import android.content.Intent
import android.view.View.VISIBLE
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityJoinNicknameBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.join.JoinConstant.ACCESS_TOKEN
import com.sopt.smeem.presentation.join.JoinConstant.NICKNAME
import com.sopt.smeem.presentation.join.JoinConstant.REFRESH_TOKEN
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class JoinWithNicknameActivity :
    BindingActivity<ActivityJoinNicknameBinding>(R.layout.activity_join_nickname) {
    private val vm: JoinNicknameVM by viewModels()

    override fun addListeners() {
        watchButtonSwitchWithTextChanging()
        onTextWrite()
        onNext()
    }

    private fun onTextWrite() {
        binding.etEntranceNickname.addTextChangedListener { watcher ->
            if (watcher.isNullOrBlank() || watcher.length > 10) {
                watchButtonSwitchWithTextChanging()
            } else {
                nextButtonOn()
            }
        }
    }

    private fun onNext() {
        binding.btnEntranceNext.setOnSingleClickListener {
            vm.content = binding.etEntranceNickname.text.toString()
            checkNicknameDuplicated()
            afterCheckNicknameDuplicated()
        }
    }


    private fun checkNicknameDuplicated() {
        vm.callApiNicknameDuplicated(
            onError = { t -> Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show() }
        )
    }

    private fun afterCheckNicknameDuplicated() {
        vm.nicknameDuplicated.observe(this@JoinWithNicknameActivity) { isDuplicated ->
            when (isDuplicated) {
                true -> {
                    binding.tvEntranceNicknameDuplicated.visibility = VISIBLE
                    binding.btnEntranceNext.isEnabled = false
                }

                false -> {
                    val toAgreement = Intent(this, JoinWithAgreementActivity::class.java)
                    toAgreement.putExtra(NICKNAME, vm.content)
                    toAgreement.putExtra(ACCESS_TOKEN, intent.getStringExtra(ACCESS_TOKEN))
                    toAgreement.putExtra(REFRESH_TOKEN, intent.getStringExtra(REFRESH_TOKEN))
                    startActivity(toAgreement)
                    if (!isFinishing) finish()
                }
            }
        }
    }

    private fun watchButtonSwitchWithTextChanging() {
        binding.btnEntranceNext.setBackgroundColor(resources.getColor(R.color.point_inactive, null))
        binding.btnEntranceNext.isEnabled = false
    }

    private fun nextButtonOn() {
        binding.btnEntranceNext.setBackgroundColor(resources.getColor(R.color.point, null))
        binding.btnEntranceNext.isEnabled = true
    }
}