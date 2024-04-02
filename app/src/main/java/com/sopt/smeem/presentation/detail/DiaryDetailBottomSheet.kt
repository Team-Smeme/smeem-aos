package com.sopt.smeem.presentation.detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.datastore.preferences.core.edit
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.data.SmeemDataStore.RECENT_DIARY_DATE
import com.sopt.smeem.data.SmeemDataStore.dataStore
import com.sopt.smeem.databinding.BottomSheetDiaryDetailBinding
import com.sopt.smeem.description
import com.sopt.smeem.event.AmplitudeEventType
import com.sopt.smeem.presentation.EventVM
import kotlinx.coroutines.launch
import java.time.LocalDate

class DiaryDetailBottomSheet(
    private val viewModel: DiaryDetailViewModel,
    private val eventViewModel: EventVM,
) : BottomSheetDialogFragment() {
    private var _binding: BottomSheetDiaryDetailBinding? = null
    private val binding get() = requireNotNull(_binding)

    private lateinit var viewModelDate: LocalDate
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

        viewModelDate = viewModel.date.value?.toLocalDate()!!

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
            putExtra("diaryId", viewModel.diaryId)
            putExtra("originalContent", viewModel.diary.value)
            putExtra("randomTopic", viewModel.topic.value)
        }.run(::startActivity)
        eventViewModel.sendEvent(AmplitudeEventType.MY_DIARY_EDIT)
    }

    private fun showDeleteDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setCustomTitle(layoutInflater.inflate(R.layout.delete_dialog_title, null))
            .setNegativeButton("아니요") { dialog, which -> }
            .setPositiveButton("예") { dialog, which ->
                viewModel.deleteDiary(
                    onSuccess = {
                        // 오늘 작성한 일기일 때만 일기 삭제 시 datastore의 최근 일기 날짜 삭제
                        if (viewModelDate.isEqual(LocalDate.now())) {
                            lifecycleScope.launch {
                                fragmentContext.dataStore.edit { storage ->
                                    storage.remove(RECENT_DIARY_DATE)
                                }
                            }
                        }
                        viewModel.isDiaryDeleted.value = true
                        dismiss()
                    },
                    onError = { e ->
                        Toast.makeText(requireContext(), e.description(), Toast.LENGTH_SHORT).show()
                    },
                )
            }
            .show()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    companion object {
        const val TAG = "BottomSheetDiaryDetail"
    }
}
