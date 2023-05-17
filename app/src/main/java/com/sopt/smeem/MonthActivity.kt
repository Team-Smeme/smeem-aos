package com.sopt.smeem

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sopt.smeem.databinding.ActivityMonthBinding
import com.sopt.smeem.util.achievementConvertStringToDate
import java.util.*

class MonthActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMonthBinding
    private lateinit var mockCalendarDateList: MutableList<Pair<Date?, Int>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mockCalendarDateList = mutableListOf()
        var position = 0
        while (position < 31) {
            mockCalendarDateList.add(
                position,
                Pair("1999.08.06".achievementConvertStringToDate(), 1)
            )
            position += 1
        }


        binding.monthCalendar.setSmeemCountList(mockCalendarDateList)
    }
}