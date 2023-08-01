package com.sopt.smeem.util

import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.Day

object ButtonUtil {
    public fun MaterialButton.switchOn() {
        this.setIconResource(R.drawable.ic_selection_active)
        this.setTextColor(resources.getColor(R.color.point, null))
        this.setStrokeColorResource(R.color.point)
    }

    public fun MaterialButton.switchOff() {
        this.setIconResource(R.drawable.ic_selection_inactive)
        this.setTextColor(resources.getColor(R.color.gray_600, null))
        this.setStrokeColorResource(R.color.gray_100)
    }

    fun TextView.switchOff() {
        if (Day.from(this.text.toString()) == Day.MON) {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive_left)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        } else if (Day.from(this.text.toString()) == Day.SUN) {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive_right)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        } else {
            this.setBackgroundResource(R.drawable.shape_time_day_inactive)
            this.setTextColor(resources.getColor(R.color.gray_500, null))
        }
    }

    fun TextView.switchOn() {
        if (Day.from(this.text.toString()) == Day.MON) {
            this.setBackgroundResource(R.drawable.shape_time_day_active_left)
            this.setTextColor(resources.getColor(R.color.white, null))
        } else if (Day.from(this.text.toString()) == Day.SUN) {
            this.setBackgroundResource(R.drawable.shape_time_day_active_right)
            this.setTextColor(resources.getColor(R.color.white, null))
        } else {
            this.setBackgroundColor(resources.getColor(R.color.point, null))
            this.setTextColor(resources.getColor(R.color.white, null))
        }
    }
}