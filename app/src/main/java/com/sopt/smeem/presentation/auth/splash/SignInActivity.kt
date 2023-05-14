package com.sopt.smeem.presentation.auth.splash

import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivitySignInBinding
import com.sopt.smeem.presentation.BindingActivity

class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {
    lateinit var bs: LoginBottomSheet

    override fun constructLayout() {
        super.constructLayout()
        setUpBottomSheet()
    }

    override fun addListeners() {
        super.addListeners()
        onTouch()
    }

    override fun addObservers() {
        super.addObservers()
    }

    private fun setUpBottomSheet() {
        bs = LoginBottomSheet()
    }

    private fun onTouch() {
        binding.tvAlreadyAuthed.setOnClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }

        binding.btnAuthStart.setOnClickListener {
        }
    }
}