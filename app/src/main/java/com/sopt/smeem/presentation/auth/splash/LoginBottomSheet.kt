package com.sopt.smeem.presentation.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.SocialType
import com.sopt.smeem.databinding.BottomSheetAuthBinding
import com.sopt.smeem.description
import com.sopt.smeem.logging
import com.sopt.smeem.presentation.auth.LoginProcess

class LoginBottomSheet : BottomSheetDialogFragment(), LoginProcess {
    var _binding: BottomSheetAuthBinding? = null
    private val binding: BottomSheetAuthBinding
        get() = requireNotNull(_binding)

    private val vm: LoginVM by lazy {
        ViewModelProvider(requireActivity(), object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>) = LoginVM() as T
        }).get(LoginVM::class.java)
    }

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
                    onSuccess = { idToken ->
                        vm.login(idToken, SocialType.KAKAO) { exception ->
                            exception.logging("LOGIN_FAILED")
                            Toast.makeText(context, exception.description(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }

            // kakao app 이 없는 경우, web 으로 로그인 시도
            else {
                KakaoHandler.loginOnWeb(context,
                    onSuccess = { idToken ->
                        vm.login(idToken, SocialType.KAKAO) { exception ->
                            exception.logging("LOGIN_FAILED")
                            Toast.makeText(context, exception.description(), Toast.LENGTH_SHORT).show()
                        }
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetAuth"
    }
}