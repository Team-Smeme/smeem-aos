package com.sopt.smeem.presentation.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.DialogBadgeMultipleBinding
import com.sopt.smeem.databinding.DialogBadgeSingleBinding
import com.sopt.smeem.presentation.mypage.MyBadgesActivity
import com.sopt.smeem.util.setOnSingleClickListener

class BadgeDialogFragment: DialogFragment() {
    private val singleBadge by lazy { DialogBadgeSingleBinding.inflate(layoutInflater) }
    private val multipleBadge by lazy { DialogBadgeMultipleBinding.inflate(layoutInflater) }
    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        addListeners()

        // TODO: 분기처리 필요
        return createDialog(singleBadge.root)
//        return createDialog(multipleBadge.root)
    }

    private fun addListeners() {
        addSingleBadgeListeners()
        addMultipleBadgeListeners()
    }

    private fun createDialog(view: View): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.SmeemAlertDialog)
            .setView(view)
            .create()
    }

    private fun addSingleBadgeListeners() {
        singleBadge.btnBadgeSingleExit.setOnSingleClickListener {
            dismiss()
        }
        singleBadge.btnBadgeSingleMore.setOnSingleClickListener {
            Intent(requireContext(), MyBadgesActivity::class.java).run(::startActivity)
        }
    }

    private fun addMultipleBadgeListeners() {
        multipleBadge.btnBadgeMultipleExit.setOnSingleClickListener {
            // TODO: 보여줘야 할 배지가 남았는지 확인 -> 있으면 다른 dialog 띄우기
            dismiss()
        }
    }
}