package com.sopt.smeem.presentation.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.SocialType
import com.sopt.smeem.databinding.BottomSheetAuthBinding
import com.sopt.smeem.description
import com.sopt.smeem.logging
import com.sopt.smeem.presentation.auth.LoginProcess
import com.sopt.smeem.presentation.auth.KakaoHandler
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginBottomSheet : BottomSheetDialogFragment(), LoginProcess {
    var _binding: BottomSheetAuthBinding? = null
    private val vm: LoginVM by activityViewModels()
    private val binding: BottomSheetAuthBinding
        get() = requireNotNull(_binding)

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

        binding.btnKakao.setOnClickListener {
            if (KakaoHandler.isAppEnabled(context)) {
                KakaoHandler.loginOnApp(context,
                    onSuccess = { kakaoAccessToken, kakaoRefreshToken ->
                        vm.login(
                            kakaoAccessToken = kakaoAccessToken,
                            socialType = SocialType.KAKAO,
                            onError = { exception ->
                                exception.logging("LOGIN_FAILED")
                                Toast.makeText(context, exception.description(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
                    },
                    onFailed = { exception -> exception.logging("KAKAO_LOGIN") })
            }

            // kakao app 이 없는 경우, web 으로 로그인 시도
            else {
                KakaoHandler.loginOnWeb(context,
                    onSuccess = { kakaoAccessToken, kakaoRefreshToken ->
                        vm.login(
                            kakaoAccessToken = kakaoAccessToken,
                            socialType = SocialType.KAKAO,
                            onError = { exception ->
                                exception.logging("LOGIN_FAILED")
                                Toast.makeText(context, exception.description(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        )
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