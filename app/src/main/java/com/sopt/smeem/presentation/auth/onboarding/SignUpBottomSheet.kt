package com.sopt.smeem.presentation.auth.onboarding

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.SocialType
import com.sopt.smeem.databinding.BottomSheetSignUpBinding
import com.sopt.smeem.logging
import com.sopt.smeem.presentation.auth.LoginProcess
import com.sopt.smeem.presentation.auth.splash.KakaoHandler

class SignUpBottomSheet() : BottomSheetDialogFragment(), LoginProcess {
    var _binding: BottomSheetSignUpBinding? = null
    private val binding: BottomSheetSignUpBinding
        get() = requireNotNull(_binding)
    private val vm: OnBoardingVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = OnBoardingVM() as T
        })[OnBoardingVM::class.java]
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
                    onSuccess = { idToken ->
                        vm.login(
                            idToken = idToken,
                            socialType = SocialType.KAKAO,
                            onError = { e -> e.logging("LOGIN_FAILED") }
                        )
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            } else {
                KakaoHandler.loginOnWeb(
                    context,
                    onSuccess = { idToken ->
                        vm.login(
                            idToken = idToken,
                            socialType = SocialType.KAKAO,
                            onError = { e -> e.logging("LOGIN_FAILED") }
                        )
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }
        }

        binding.tvSignUpAnonymous.setOnClickListener {
            vm.goAnonymous()
            Toast.makeText(requireContext(), "비회원 시작", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetSignUp"
    }
}