package com.sopt.smeem.presentation.auth.entrance

import android.content.Intent
import android.view.View.VISIBLE
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityEntranceNicknameBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.auth.entrance.EntranceConstant.NICKNAME

class EntranceNicknameActivity :
    BindingActivity<ActivityEntranceNicknameBinding>(R.layout.activity_entrance_nickname) {
    private val vm: EntranceNicknameVM by viewModels()

    override fun constructLayout() {
        nextButtonOff()
    }

    override fun addListeners() {
        onTextWrite()
        onNext()
    }

    private fun onTextWrite() {
        binding.etEntranceNickname.addTextChangedListener { watcher ->
            if (watcher!!.length <= 0 || watcher!!.length > 16) {
                nextButtonOff()
            } else {
                nextButtonOn()
            }
        }
    }

    private fun onNext() {
        binding.btnEntranceNext.setOnClickListener {
            if (nicknameDuplicated()) {
                binding.tvEntranceNicknameDuplicated.visibility = VISIBLE
                binding.btnEntranceNext.isEnabled = false
            } else {
                val toAgreement = Intent(this, EntranceAgreementActivity::class.java)
                toAgreement.putExtra(NICKNAME, vm.content)
                startActivity(toAgreement)
                if (!isFinishing) finish()
            }
        }
    }

    private fun nicknameDuplicated() = false // TODO : nickname 중복 api call

    private fun nextButtonOff() {
        binding.btnEntranceNext.setBackgroundColor(resources.getColor(R.color.point_inactive, null))
        binding.btnEntranceNext.isEnabled = false
    }

    private fun nextButtonOn() {
        binding.btnEntranceNext.setBackgroundColor(resources.getColor(R.color.point, null))
        binding.btnEntranceNext.isEnabled = true
    }
}