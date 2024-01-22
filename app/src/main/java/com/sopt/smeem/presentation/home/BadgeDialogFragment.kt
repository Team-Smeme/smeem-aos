package com.sopt.smeem.presentation.home

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.sopt.smeem.R
import com.sopt.smeem.databinding.DialogBadgeBinding
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.mypage.MyBadgesActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class BadgeDialogFragment: DialogFragment() {
    private val binding by lazy<DialogBadgeBinding> {
        DataBindingUtil.inflate(requireActivity().layoutInflater, R.layout.dialog_badge, null, false)
    }
    private val viewModel by viewModels<HomeViewModel>()
    private val eventVm: EventVM by viewModels()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        constructLayout()
        addListeners()

        return createDialog(binding.root)
    }

    private fun constructLayout() {
        val name = requireArguments().getString(BADGE_NAME) as String
        val imageUrl = requireArguments().getString(BADGE_IMAGE_URL) as String
        val isFirstBadge = requireArguments().getBoolean(IS_FIRST_BADGE)

        initDataBinding()
        initBadgeData(name, imageUrl, isFirstBadge)
    }

    private fun addListeners() {
        binding.btnBadgeExit.setOnSingleClickListener {
            dismiss()

            if(viewModel.nowBadge == "첫 번째 일기") {
                eventVm.sendEvent(AmplitudeEventType.WELCOME_QUIT_CLICK)
            }
        }
        binding.btnBadgeMore.setOnSingleClickListener {
            Intent(requireContext(), MyBadgesActivity::class.java).run(::startActivity)
            dismiss()
            eventVm.sendEvent(AmplitudeEventType.BADGE_MORE_CLICK)

            if(viewModel.nowBadge == "첫 번째 일기") {
                eventVm.sendEvent(AmplitudeEventType.WELCOME_MORE_CLICK)
            }
        }
    }

    private fun createDialog(view: View): Dialog {
        return AlertDialog.Builder(requireContext(), R.style.SmeemAlertDialog)
            .setView(view)
            .create()
    }

    private fun initDataBinding() {
        with(binding) {
            vm = viewModel
            lifecycleOwner = requireActivity()
        }
    }

    private fun initBadgeData(name: String, imageUrl: String, isFirstBadge: Boolean) {
        viewModel.setBadgeInfo(name, imageUrl, isFirstBadge)
    }

    companion object {
        private const val BADGE_NAME = "badgeName"
        private const val BADGE_IMAGE_URL = "badgeImageUrl"
        private const val IS_FIRST_BADGE = "isFirstBadge"

        fun newInstance(name: String, imageUrl: String, isFirstBadge: Boolean) : BadgeDialogFragment {
            val args = Bundle().apply {
                putString(BADGE_NAME, name)
                putString(BADGE_IMAGE_URL, imageUrl)
                putBoolean(IS_FIRST_BADGE, isFirstBadge)
            }
            BadgeDialogFragment()
                .apply { arguments = args }
                .run { return this }
        }
    }
}