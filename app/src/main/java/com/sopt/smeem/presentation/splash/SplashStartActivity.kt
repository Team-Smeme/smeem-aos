package com.sopt.smeem.presentation.splash

import android.content.Intent
import androidx.activity.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivitySplashStartBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashStartActivity :
    BindingActivity<ActivitySplashStartBinding>(R.layout.activity_splash_start) {
    private val vm: SplashVM by viewModels()

    override fun constructLayout() {
        vm.checkAuthed()
    }

    override fun addObservers() {
        observeAuthed()
    }

    private fun observeAuthed() {
        vm.isAuthed.observe(this) {
            when (it) {
                true -> { // Home 으로 이동
                    startActivity(Intent(this@SplashStartActivity, HomeActivity::class.java))
                    finish()
                }

                false -> { // Login Splash 로 이동
                    startActivity(Intent(this@SplashStartActivity, SplashLoginActivity::class.java))
                    finish()
                }
            }
        }
    }


}