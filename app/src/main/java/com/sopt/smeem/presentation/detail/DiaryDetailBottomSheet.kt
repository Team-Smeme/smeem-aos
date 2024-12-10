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
import com.sopt.smeem.presentation.IntentConstants.HAS_CORRECTIONS
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
            checkDiaryCoached()
        }
        binding.tvDelete.setOnClickListener {
            showDeleteDialog()
        }
    }

    private fun checkDiaryCoached() {
        viewModel.diaryDetailResult.observe(viewLifecycleOwner) { diaryDetail ->
            if (diaryDetail.hasCorrections) {
                showEditAlertDialog()
            } else {
                moveToEdit()
            }
        }
    }

    private fun showEditAlertDialog() {
        MaterialAlertDialogBuilder(fragmentContext)
            .setCustomTitle(layoutInflater.inflate(R.layout.edit_dialog_title, binding.root, false))
            .setView(layoutInflater.inflate(R.layout.edit_dialog_message, binding.root, false))
            .setNegativeButton("아니요") { _, _ -> dismiss() }
            .setPositiveButton("예") { _, _ ->
                moveToEdit()
                dismiss()
            }
            .show()
    }

    private fun moveToEdit() {
        Intent(fragmentContext, DiaryEditActivity::class.java).apply {
            putExtra(DIARY_ID, viewModel.getDiaryId())
            putExtra(HAS_CORRECTIONS, viewModel.diaryDetailResult.value?.hasCorrections!!)
            putExtra(ORIGINAL_CONTENT, viewModel.getContent())
            putExtra(RANDOM_TOPIC, viewModel.getTopic())
        }.also { intent ->
            startActivity(intent)
            dismiss()
        }
        eventViewModel.sendEvent(
            AmplitudeEventType.MY_DIARY_EDIT,
            mapOf("has_coaching" to viewModel.diaryDetailResult.value?.hasCorrections!!)
        )
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(fragmentContext)
            .setCustomTitle(
                layoutInflater.inflate(
                    R.layout.delete_dialog_title,
                    binding.root,
                    false
                )
            )
            .setNegativeButton("아니요") { _, _ -> dismiss() }
            .setPositiveButton("예") { _, _ ->
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
