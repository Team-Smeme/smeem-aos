package com.sopt.smeem.presentation.auth.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.user.UserApiClient
import com.sopt.smeem.databinding.BottomSheetAuthBinding

class LoginBottomSheet : BottomSheetDialogFragment() {
    var _binding : BottomSheetAuthBinding? = null
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
        // TODO : 구현만 확인, 이후 코드 정리 및 절차별 분기 구현 필요
        binding.btnKakao.setOnClickListener {
            if(UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                UserApiClient.instance.loginWithKakaoTalk(requireContext()) { token, error ->
                    if(error != null) {
                        // login failed
                        Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                    }
                    else if (token != null) {
                        // login succeed
                        Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                    }
                }
            }
            else {
                UserApiClient.instance.loginWithKakaoAccount(context, callback = {token, error ->
                    run {
                        if (error != null) {
                            // login failed
                            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
                        } else if (token != null) {
                            // login succeed
                            Toast.makeText(context, "로그인 성공", Toast.LENGTH_SHORT).show()
                        }
                    }
                })
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