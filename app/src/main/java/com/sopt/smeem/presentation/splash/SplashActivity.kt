package com.sopt.smeem.presentation.splash

import android.content.Intent
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivitySplashBinding
import com.sopt.smeem.domain.model.LoginResult
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.join.JoinWithNicknameActivity
import com.sopt.smeem.presentation.onboarding.OnBoardingActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : BindingActivity<ActivitySplashBinding>(R.layout.activity_splash) {
    lateinit var bs: LoginBottomSheet
    private val vm: LoginVM by viewModels()

    override fun constructLayout() {
        super.constructLayout()
        bs = LoginBottomSheet()
    }

    override fun addListeners() {
        super.addListeners()
        onAutoAuthed()
        onTouchAlreadyAuthed() // LoginBottomSheet
        onTouchStart() // OnBoarding 으로 이동
    }

    override fun addObservers() {
        afterLoginSuccess()
    }

    private fun onTouchStart() {
        binding.btnAuthStart.setOnSingleClickListener {
            val toOnBoarding = Intent(this, OnBoardingActivity::class.java)
            startActivity(toOnBoarding)

            if (!isFinishing) finish()
        }
    }

    private fun onAutoAuthed() {
        when(vm.isAuthed()) {
            true -> {
                // TODO : isRegistered, hasPlan 값들도 DataStore 에 저장해 온보딩중 이탈 회원 자동으로 이동하도록 (QA fix)
                startActivity(Intent(this@SplashActivity, HomeActivity::class.java))
                finish()
            }
            false -> {
                // do nothing
            }
        }
    }

    private fun onTouchAlreadyAuthed() {
        binding.tvAlreadyAuthed.setOnSingleClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }
    }

    private fun afterLoginSuccess() {
        vm.loginResult.observe(this@SplashActivity) {
            when (it.isRegistered) {
                true -> gotoHome() // 회원 정보 등록까지 완료된 상태라면, Home 으로 이동
                false -> gotoNext(it) // 회원 정보 등록이 되지 않은 상태 (학습 목표, 닉네임 세팅이 되어있는지에 따라 분기)
            }
        }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun gotoNext(loginResult: LoginResult) {
        when (loginResult.isPlanRegistered) {
            true -> gotoJoin() // 학습 목표는 세팅되어있다면, Join 으로 이동 (닉네임 입력)
            false -> gotoOnBoarding() // 학습 목표가 세팅되어있지 않다면, OnBoarding 으로 이동
        }
    }

    private fun gotoJoin() {
        startActivity(Intent(this@SplashActivity, JoinWithNicknameActivity::class.java))
        if (!isFinishing) finish()
    }

    private fun gotoOnBoarding() {
        startActivity(Intent(this@SplashActivity, OnBoardingActivity::class.java))
        if (!isFinishing) finish()
    }
}