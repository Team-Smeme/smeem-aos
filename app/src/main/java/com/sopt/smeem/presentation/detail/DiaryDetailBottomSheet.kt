package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.databinding.BottomSheetDiaryDetailBinding

class DiaryDetailBottomSheet() : BottomSheetDialogFragment() {
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
            // TODO: 일기 내용 담아서 수정 뷰로 보내기
            putExtra("originalContent", "원본 일기 내용을 넣어주세요")
        }
        startActivity(intent)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle("일기를 삭제할까요?")
            .setNegativeButton("예") { dialog, which ->
                // TODO: 삭제 api 호출
                // TODO: 홈화면으로 돌아가는 로직
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