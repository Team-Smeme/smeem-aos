package com.sopt.smeem.presentation.splash

import android.content.Intent
import android.content.res.ColorStateList
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivitySplashLoginBinding
import com.sopt.smeem.domain.dto.LoginResultDto
import com.sopt.smeem.event.AmplitudeEventType.FIRST_VIEW
import com.sopt.smeem.event.AmplitudeEventType.SIGN_UP_SUCCESS
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.base.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.join.JoinConstant.ACCESS_TOKEN
import com.sopt.smeem.presentation.join.JoinConstant.REFRESH_TOKEN
import com.sopt.smeem.presentation.join.JoinWithNicknameActivity
import com.sopt.smeem.presentation.onboarding.OnBoardingActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashLoginActivity :
    BindingActivity<ActivitySplashLoginBinding>(R.layout.activity_splash_login) {
    lateinit var bs: LoginBottomSheet
    private val vm: LoginVM by viewModels()
    private val eventVm: EventVM by viewModels()
    private var backPressedTime: Long = 0

    override fun constructLayout() {
        super.constructLayout()
        bs = LoginBottomSheet()
        binding.ivSignInHeader.imageTintList =
            ColorStateList.valueOf(resources.getColor(R.color.point, null))

        initBackPressedCallback()
        eventVm.sendEvent(FIRST_VIEW)
    }

    override fun addListeners() {
        super.addListeners()
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

    private fun onTouchAlreadyAuthed() {
        binding.tvAlreadyAuthed.setOnSingleClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }
    }

    private fun afterLoginSuccess() {
        vm.loginResult.observe(this@SplashLoginActivity) {
            when (it.isRegistered) {
                true -> {
                    vm.saveTokenOnLocal(it.apiAccessToken, it.apiRefreshToken)
                    gotoHome() // 회원 정보 등록까지 완료된 상태라면, Home 으로 이동
                }

                false -> {
                    eventVm.sendEvent(SIGN_UP_SUCCESS)
                    gotoNext(it) // 회원 정보 등록이 되지 않은 상태 (학습 목표, 닉네임 세팅이 되어있는지에 따라 분기)}
                }
            }
        }
    }

    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun gotoNext(loginResult: LoginResultDto) {
        when (loginResult.isPlanRegistered) {
            true -> gotoJoin(
                loginResult.apiAccessToken,
                loginResult.apiRefreshToken
            ) // 학습 목표는 세팅되어있다면, Join 으로 이동 (닉네임 입력)
            false -> gotoOnBoarding(
                loginResult.apiAccessToken,
                loginResult.apiRefreshToken,
            ) // 학습 목표가 세팅되어있지 않다면, OnBoarding 으로 이동
        }
    }

    private fun gotoJoin(accessToken: String, refreshToken: String) {
        val toJoin = Intent(this@SplashLoginActivity, JoinWithNicknameActivity::class.java)
        toJoin.putExtra(ACCESS_TOKEN, accessToken)
        toJoin.putExtra(REFRESH_TOKEN, refreshToken)
        startActivity(intent)

        if (!isFinishing) finish()
    }

    private fun gotoOnBoarding(accessToken: String, refreshToken: String) {
        val toOnBoarding = Intent(this@SplashLoginActivity, OnBoardingActivity::class.java)
        toOnBoarding.putExtra(ACCESS_TOKEN, accessToken)
        toOnBoarding.putExtra(REFRESH_TOKEN, refreshToken)
        startActivity(toOnBoarding)

        if (!isFinishing) finish()
    }

    private fun initBackPressedCallback() {
        val onBackPressedCallback =
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    if (System.currentTimeMillis() - backPressedTime >= BACK_PRESSED_INTERVAL) {
                        backPressedTime = System.currentTimeMillis()
                        Snackbar.make(
                            binding.root,
                            getString(R.string.notice_back_process),
                            Snackbar.LENGTH_SHORT,
                        ).show()
                    } else {
                        finish()
                    }
                }
            }
        onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
    }

    companion object {
        const val BACK_PRESSED_INTERVAL = 2000
    }
}