package com.sopt.smeem.presentation.auth.splash

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.databinding.BottomSheetAuthBinding
import com.sopt.smeem.logging
import com.sopt.smeem.presentation.auth.LoginProcess
import com.sopt.smeem.presentation.auth.LoginResult
import com.sopt.smeem.presentation.auth.entrance.EntranceNicknameActivity
import com.sopt.smeem.presentation.auth.onboarding.OnBoardingActivity

class LoginBottomSheet : BottomSheetDialogFragment(), LoginProcess {
    var _binding: BottomSheetAuthBinding? = null
    private val binding: BottomSheetAuthBinding
        get() = requireNotNull(_binding)
    lateinit var loginResult: LoginResult

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetAuthBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        binding.tvKakao.setOnClickListener {
            if (KakaoHandler.isAppEnabled(context)) {
                KakaoHandler.loginOnApp(context,
                    onSuccess = { accessToken, refreshToken ->
                        loginResult = sendServer(requireContext(), accessToken)
                        doAfterLoginSuccess()
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }

            // kakao app 이 없는 경우, web 으로 로그인 시도
            else {
                KakaoHandler.loginOnWeb(context,
                    onSuccess = { accessToken, refreshToken ->
                        loginResult = sendServer(requireContext(), accessToken)
                        doAfterLoginSuccess()
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun gotoHome() {
        // TODO : go to home activity
    }

    private fun gotoNicknameEntrance() {
        val goToNicknameEntrance = Intent(context, EntranceNicknameActivity::class.java)
        startActivity(goToNicknameEntrance)  // sign in activity already finished
    }

    private fun gotoPlanOnBoarding() {
        val goToOnBoarding = Intent(context, OnBoardingActivity::class.java)
        startActivity(goToOnBoarding)  // sign in activity already finished
    }

    override fun doAfterLoginSuccess() {
        when (loginResult.isRegistered) {
            true -> gotoHome() // 이미 등록된 경우라면, 메인으로 바로 이동
            false -> {
                when (loginResult.isPlanRegistered) {
                    true -> gotoNicknameEntrance() // plan 이 등록된 상태라면, nickname entrance 로 이동
                    false -> gotoPlanOnBoarding() // plan 이 등록되지 않은 상태라면, plan onBoarding 으로 이동
                }
            }
        }
        onDestroy()
    }

    companion object {
        const val TAG = "BottomSheetAuth"
    }
}