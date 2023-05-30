package com.sopt.smeem.calendar

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

//        binding.weekCalendar.visibility = View.VISIBLE
//        binding.monthCalendar.visibility = View.GONE

        val topSheet = binding.coordinator.findViewById<View>(R.id.top_sheet)
        val bottomSheetBehavior: BottomSheetBehavior<View> = BottomSheetBehavior.from(topSheet)

        // 상단 위치 지정
        bottomSheetBehavior.peekHeight = resources.getDimensionPixelSize(R.dimen.actionBarSize)

        // Bottom Sheet가 숨겨질 수 없게 지정
        bottomSheetBehavior.isHideable = false
//        bottomSheetBehavior.addBottomSheetCallback(object :
//            BottomSheetBehavior.BottomSheetCallback() {
//            override fun onStateChanged(bottomSheet: View, newState: Int) {
//                when (newState) {
//                    // BottomSheet가 당겨지면 Monthly Calendar로 전환
//                    BottomSheetBehavior.STATE_HIDDEN -> {
//                        binding.monthCalendar.visibility = View.VISIBLE
//                        binding.weekCalendar.visibility = View.GONE
//                    }
//
//                    // BottomSheet가 원래 위치로 이동하면 Weekly Calendar로 전환
//                    BottomSheetBehavior.STATE_EXPANDED -> {
//                        binding.monthCalendar.visibility = View.GONE
//                        binding.weekCalendar.visibility = View.VISIBLE
//                    }
//
//                    // 원하는 로직에 따라 이벤트 추가
//                    else -> {}
//                }
//            }
//
//            override fun onSlide(bottomSheet: View, slideOffset: Float) {
//                // 슬라이딩 이벤트 처리
//            }
//        }
    }


}