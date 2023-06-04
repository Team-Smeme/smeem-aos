package com.sopt.smeem.presentation.auth.onboarding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.Day
import com.sopt.smeem.databinding.BottomSheetSignUpBinding
import com.sopt.smeem.description
import com.sopt.smeem.logging
import com.sopt.smeem.presentation.auth.LoginProcess
import com.sopt.smeem.presentation.auth.LoginResult
import com.sopt.smeem.presentation.auth.entrance.EntranceNicknameActivity
import com.sopt.smeem.presentation.auth.splash.KakaoHandler

class SignUpBottomSheet : BottomSheetDialogFragment(), LoginProcess {
    lateinit var loginResult: LoginResult
    var _binding: BottomSheetSignUpBinding? = null
    private val binding: BottomSheetSignUpBinding
        get() = requireNotNull(_binding)
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        }).get(OnBoardingVM::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val context = requireContext()

        binding.layoutKakaoStart.setOnClickListener {

            if (KakaoHandler.isAppEnabled(context)) {
                KakaoHandler.loginOnApp(context,
                    onSuccess = { accessToken, refreshToken ->
                        loginResult = sendServer(requireContext(), accessToken)
                        doAfterLoginSuccess()
                    },
                    onFailed = { exception ->
                        exception.logging("KAKAO_LOGIN")
                    })
            } else {
                KakaoHandler.loginOnWeb(
                    context,
                    onSuccess = { accessToken, refreshToken ->
                        loginResult = sendServer(requireContext(), accessToken)
                        doAfterLoginSuccess()
                    },
                    onFailed = { exception ->
                        exception.logging("KAKAO_LOGIN")
                    })
            }
        }

        binding.tvSignUpAnonymous.setOnClickListener {
            gotoHome() // no token on store
            Toast.makeText(requireContext(), "비회원 시작", Toast.LENGTH_SHORT).show()
        }
    }

    override fun doAfterLoginSuccess() {
        if (loginResult.isRegistered) {
            gotoHome()
        } else {
            sendServer(vm.selectedGoal.value!!, vm.days, vm.hour, vm.minute)
            val toEntrance = Intent(requireContext(), EntranceNicknameActivity::class.java)
            startActivity(toEntrance)
            activity?.finish()
        }
    }

    private fun gotoHome() {
        // TODO : HomeActivity 로 이동
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun sendServer(selection: GoalSelection, days: List<Day>, hour: Int, minute: Int) {
        // TODO : PATCH 학습계획설정 with token on header included by store
    }

    companion object {
        const val TAG = "BottomSheetSignUp"
    }
}