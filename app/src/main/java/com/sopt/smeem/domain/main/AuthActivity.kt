package com.sopt.smeem.domain.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sopt.smeem.databinding.ActivityAuthBinding

class AuthActivity : AppCompatActivity() {
    lateinit var binding: ActivityAuthBinding
    lateinit var bs: AuthBottomSheet

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpBottomSheet()
        onTouch()
    }

    private fun setUpBottomSheet() {
        bs = AuthBottomSheet()
    }


    private fun onTouch() {
        binding.btnAuthStart.setOnClickListener {
            bs.show(supportFragmentManager, AuthBottomSheet.TAG)
        }
    }
}