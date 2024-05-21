package com.sopt.smeem.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.databinding.BottomSheetDiaryDetailBinding
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import com.sopt.smeem.presentation.IntentConstants.DIARY_ID
import com.sopt.smeem.presentation.IntentConstants.ORIGINAL_CONTENT
import com.sopt.smeem.presentation.IntentConstants.RANDOM_TOPIC

class DiaryDetailBottomSheet(
    private val viewModel: DiaryDetailViewModel,
    private val eventViewModel: EventVM,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDiaryDetailBinding? = null
    private val binding get() = requireNotNull(_binding)
    private lateinit var fragmentContext: Context

    override fun onAttach(context: Context) {
        super.onAttach(context)
        fragmentContext = context
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = BottomSheetDiaryDetailBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addListeners()
    }

    fun addListeners() {
        binding.tvEdit.setOnClickListener {
            dismiss()
            moveToEdit()
        }
        binding.tvDelete.setOnClickListener {
            dismiss()
            showDeleteDialog()
        }
    }

    private fun moveToEdit() {
        Intent(requireContext(), DiaryEditActivity::class.java).apply {
            putExtra(DIARY_ID, viewModel.getDiaryId())
            putExtra(ORIGINAL_CONTENT, viewModel.getContent())
            putExtra(RANDOM_TOPIC, viewModel.getTopic())
        }.run(::startActivity)
        eventViewModel.sendEvent(AmplitudeEventType.MY_DIARY_EDIT)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(layoutInflater.inflate(R.layout.delete_dialog_title, null))
            .setNegativeButton("아니요") { dialog, which -> }
            .setPositiveButton("예") { dialog, which ->
                viewModel.deleteDiary(
                    onSuccess = { dismiss() },
                    onError = { t ->
                        Toast.makeText(requireContext(), t.message, Toast.LENGTH_SHORT).show()
                    },
                )
            }
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetDiaryDetail"
    }
}
