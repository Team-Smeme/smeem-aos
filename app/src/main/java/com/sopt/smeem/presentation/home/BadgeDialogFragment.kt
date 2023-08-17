package com.sopt.smeem.presentation.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.DialogBadgeMultipleBinding
import com.sopt.smeem.databinding.DialogBadgeSingleBinding
import com.sopt.smeem.presentation.mypage.MyBadgesActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeDialogFragment: DialogFragment() {
    private var _single: DialogBadgeSingleBinding? = null
    private val single get() = requireNotNull(_single) { "value of _single is null" }
    private var _multiple: DialogBadgeMultipleBinding? = null
    private val multiple get() = requireNotNull(_multiple) { "value of _multiple is null" }

    private val viewModel by viewModels<HomeViewModel>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val name = requireArguments().getString(BADGE_NAME) as String
        val imageUrl = requireArguments().getString(BADGE_IMAGE_URL) as String
        val isFirstBadge = requireArguments().getBoolean(IS_FIRST_BADGE)

        // TODO: 로직 개선하기..
        return if (isFirstBadge) {
            _single = DataBindingUtil.inflate(requireActivity().layoutInflater, R.layout.dialog_badge_single, null, false)
            with(single) {
                vm = viewModel
                lifecycleOwner = requireActivity()
            }
            addSingleBadgeListeners()
            viewModel.setBadgeInfo(name, imageUrl)
            createDialog(single.root)
        } else {
            _multiple = DataBindingUtil.inflate(requireActivity().layoutInflater, R.layout.dialog_badge_multiple, null, false)
            with(multiple) {
                vm = viewModel
                lifecycleOwner = requireActivity()
            }
            addMultipleBadgeListeners()
            viewModel.setBadgeInfo(name, imageUrl)
            createDialog(multiple.root)
        }
    }

    private fun createDialog(view: View): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.SmeemAlertDialog)
            .setView(view)
            .create()
    }

    private fun addSingleBadgeListeners() {
        single.btnBadgeSingleExit.setOnSingleClickListener {
            dismiss()
        }
        single.btnBadgeSingleMore.setOnSingleClickListener {
            Intent(requireContext(), MyBadgesActivity::class.java).run(::startActivity)
        }
    }

    private fun addMultipleBadgeListeners() {
        multiple.btnBadgeMultipleExit.setOnSingleClickListener {
            dismiss()
        }
    }

    companion object {
        private const val BADGE_NAME = "badgeName"
        private const val BADGE_IMAGE_URL = "badgeImageUrl"
        private const val IS_FIRST_BADGE = "isLastBadge"
        fun newInstance(name: String, imageUrl: String, isFirstBadge: Boolean) : BadgeDialogFragment {
            val fragment = BadgeDialogFragment()
            val args = Bundle().apply {
                putString(BADGE_NAME, name)
                putString(BADGE_IMAGE_URL, imageUrl)
                putBoolean(IS_FIRST_BADGE, isFirstBadge)
            }
            fragment.arguments = args
            return fragment
        }
    }
}