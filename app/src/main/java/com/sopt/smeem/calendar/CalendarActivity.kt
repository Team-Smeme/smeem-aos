package com.sopt.smeem.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.sopt.smeem.databinding.ActivityCalendarBinding

class CalendarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCalendarBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCalendarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initializePersistentBottomSheet()
    }

    private fun initializePersistentBottomSheet() {
        val topSheetBehavior = TopSheetBehavior.from(binding.topSheetView)
        binding.clCalendar.bringToFront()
        topSheetBehavior.peekHeight = 200

        topSheetBehavior.addTopSheetCallback(object : TopSheetBehavior.SheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    TopSheetBehavior.STATE_HIDDEN -> {
                        Log.d("MainActivity", "state: hidden")
                    }

                    TopSheetBehavior.STATE_EXPANDED -> {
                        Log.d("MainActivity", "state: expanded")
                    }

                    TopSheetBehavior.STATE_COLLAPSED -> {
                        Log.d("MainActivity", "state: collapsed")
                    }

                    TopSheetBehavior.STATE_DRAGGING -> {
                        Log.d("MainActivity", "state: dragging")
                    }

                    TopSheetBehavior.STATE_SETTLING -> {
                        Log.d("MainActivity", "state: settling")
                    }

                    TopSheetBehavior.STATE_HALF_EXPANDED -> {
                        Log.d("MainActivity", "state: half expanded")
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                TODO("Not yet implemented")
            }

        })
    }
}