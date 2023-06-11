package com.sopt.smeem.presentation.auth.splash

import android.content.Intent
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityLogInBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.auth.entrance.EntranceNicknameActivity
import com.sopt.smeem.presentation.auth.onboarding.OnBoardingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLogInBinding>(R.layout.activity_log_in) {
    lateinit var bs: LoginBottomSheet
    private val vm: LoginVM by viewModels()

    override fun constructLayout() {
        super.constructLayout()
        setUpBottomSheet()
    }

    override fun addListeners() {
        super.addListeners()
        onTouch()
    }

    override fun addObservers() {
        doAfterLoginSuccess()
    }

    private fun setUpBottomSheet() {
        bs = LoginBottomSheet()
    }

    private fun onTouch() {
        binding.tvAlreadyAuthed.setOnClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }

        binding.btnAuthStart.setOnClickListener {
            val toOnBoarding = Intent(this, OnBoardingActivity::class.java)
            startActivity(toOnBoarding)

            if (!isFinishing) finish()
        }
    }

    private fun gotoHome() {
        // TODO : go to home activity
    }

    private fun gotoNicknameEntrance() {
        val goToNicknameEntrance = Intent(this@LoginActivity, EntranceNicknameActivity::class.java)
        startActivity(goToNicknameEntrance)

        if (!isFinishing) finish()
    }

    private fun gotoOnBoarding() {
        val goToOnBoarding = Intent(this@LoginActivity, OnBoardingActivity::class.java)
        startActivity(goToOnBoarding)

        if (!isFinishing) finish()
    }

    private fun doAfterLoginSuccess() {
        vm.loginResult.observe(this@LoginActivity) {
            when (it.isRegistered) {
                true -> gotoHome()
                false -> {
                    when (it.isPlanRegistered) {
                        true -> gotoNicknameEntrance()
                        false -> gotoOnBoarding()
                    }
                }
            }
        }
    }
}