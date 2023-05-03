package com.sopt.smeem.presentation.auth.splash

import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivitySignInBinding
import com.sopt.smeem.presentation.BindingActivity

class SignInActivity : BindingActivity<ActivitySignInBinding>(R.layout.activity_sign_in) {

    lateinit var bs: LoginBottomSheet

    private fun setUpBottomSheet() {
        bs = LoginBottomSheet()
    }


    private fun onTouch() {
        binding.btnAuthStart.setOnClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }
    }

    override fun constructLayout() {
        super.constructLayout()

        setUpBottomSheet()
    }

    override fun addListeners() {
        super.addListeners()
    }

    override fun addObservers() {
        super.addObservers()
    }
}