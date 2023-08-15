package com.sopt.smeem.presentation.detail

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.setFragmentResult
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.databinding.BottomSheetDiaryDetailBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.home.HomeActivity

class DiaryDetailBottomSheet(
    private val viewModel: DiaryDetailViewModel
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDiaryDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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
            moveToEdit()
            dismiss()
        }
        binding.tvDelete.setOnClickListener {
            dismiss()
            showDeleteDialog()
        }
    }

    private fun moveToEdit() {
        val intent = Intent(requireContext(), DiaryEditActivity::class.java).apply {
            putExtra("diaryId", viewModel.diaryId)
            putExtra("originalContent", viewModel.diary.value)
            putExtra("randomTopic", viewModel.topic.value)
        }
        startActivity(intent)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(layoutInflater.inflate(R.layout.delete_dialog_title, null))
            .setNegativeButton("예") { dialog, which ->
                viewModel.deleteDiary(
                    onSuccess = {
                        viewModel.isDiaryDeleted.value = true
                        dismiss()
                    },
                    onError = { e ->
                        Toast.makeText(requireContext(), e.description(), Toast.LENGTH_SHORT).show()
                    }
                )
            }
            .setPositiveButton("아니요") { dialog, which -> }
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