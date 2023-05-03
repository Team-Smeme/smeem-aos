package com.sopt.smeem.presentation.auth.login

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sopt.smeem.databinding.ActivityAuthBinding

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    lateinit var bs: LoginBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomSheet()
        onTouch()
    }

    private fun setUpBottomSheet() {
        bs = LoginBottomSheet()
    }


    private fun onTouch() {
        binding.btnAuthStart.setOnClickListener {
            bs.show(supportFragmentManager, LoginBottomSheet.TAG)
        }
    }
}