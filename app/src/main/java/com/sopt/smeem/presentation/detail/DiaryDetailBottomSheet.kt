package com.sopt.smeem.presentation.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
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
            // TODO: 삭제 다이얼로그 ㄱㄱ
        }
    }

    private fun moveToEdit() {
        val intent = Intent(requireContext(), DiaryEditActivity::class.java).apply {
            // TODO: 일기 내용 담아서 수정 뷰로 보내기
            putExtra("originalContent", "원본 일기 내용을 넣어주세요")
        }
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG = "BottomSheetDiaryDetail"
    }
}