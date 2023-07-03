package com.sopt.smeem.calendar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sopt.smeem.databinding.BottomSheetWritingBinding


class WritingBottomSheet : BottomSheetDialogFragment() {
    var _binding: BottomSheetWritingBinding? = null
    private val binding: BottomSheetWritingBinding
        get() = requireNotNull(_binding)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = BottomSheetWritingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutHomeBottomForeign.setOnClickListener {
//            val goToForeign = Intent(activity, ForeignWriteActivity::class.java)
//            startActivity(goToForeign)
            // TODO : activity finish
        }

        binding.layoutHomeBottomNative.setOnClickListener {
//            val goToForeign = Intent(activity, NativeWriteStep1Activity::class.java)
//            startActivity(goToForeign)
            // TODO : activity finish
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}