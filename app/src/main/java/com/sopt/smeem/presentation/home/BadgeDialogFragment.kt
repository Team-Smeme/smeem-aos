package com.sopt.smeem.presentation.home

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.sopt.smeem.R
import com.sopt.smeem.databinding.DialogBadgeSingleBinding

class BadgeDialogFragment: DialogFragment() {
    private val badge by lazy { DialogBadgeSingleBinding.inflate(layoutInflater) }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.SmeemAlertDialog)
            .setView(badge.root)
            .create()
    }
}