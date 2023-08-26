package com.sopt.smeem.presentation.mypage

import android.content.Intent
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityMyPageMoreBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.splash.SplashLoginActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MyPageMoreActivity :
    BindingActivity<ActivityMyPageMoreBinding>(R.layout.activity_my_page_more) {

    private val viewModel by viewModels<MyPageMoreViewModel>()

    override fun addListeners() {
        binding.btnMyPageMoreBack.setOnSingleClickListener {
            finish()
        }
        binding.tvMyPageMoreManual.setOnSingleClickListener {
            // TODO
        }
        binding.tvMyPageMoreLogout.setOnSingleClickListener {
            showLogoutDialog()
        }
        binding.tvMyPageMoreDelete.setOnSingleClickListener {
            showDeleteDialog()
        }
    }

    private fun showLogoutDialog() {
        MaterialAlertDialogBuilder(this)
            .setCustomTitle(layoutInflater.inflate(R.layout.logout_dialog_content, null))
            .setNegativeButton("아니요") { dialog, which -> }
            .setPositiveButton("예") { dialog, which ->
                viewModel.clearLocal()
                startActivity(Intent(this, SplashLoginActivity::class.java))
                finishAffinity()
            }
            .show()
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(this)
            .setCustomTitle(layoutInflater.inflate(R.layout.account_delete_dialog_content, null))
            .setNegativeButton("아니요") { dialog, which -> }
            .setPositiveButton("예") { dialog, which ->
                viewModel.withdrawal()
                startActivity(Intent(this, SplashLoginActivity::class.java))
                finishAffinity()
            }
            .show()
    }
}