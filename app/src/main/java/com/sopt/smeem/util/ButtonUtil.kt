package com.sopt.smeem.util

import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.domain.model.Day

object ButtonUtil {
    public fun MaterialButton.switchOn() {
        this.setIconResource(R.drawable.ic_selection_active)
        this.setTextAppearance(R.style.TextAppearance_Smeem_Body3_medium_button_on)
        this.setTextColor(resources.getColor(R.color.point, null))
        this.setStrokeColorResource(R.color.point)
    }

    public fun MaterialButton.switchOff() {
        this.setIconResource(R.drawable.ic_selection_inactive)
        this.setTextAppearance(R.style.TextAppearance_Smeem_Body3_medium)
        this.setTextColor(resources.getColor(R.color.gray_600, null))
        this.setStrokeColorResource(R.color.gray_100)
    }

    fun TextView.switchOff() {
        when (Day.from(this.text.toString())) {
            Day.MON -> this.setBackgroundResource(R.drawable.shape_time_day_inactive_left)
            Day.SUN -> this.setBackgroundResource(R.drawable.shape_time_day_inactive_right)
            else -> this.setBackgroundResource(R.drawable.shape_time_day_inactive)
        }
        this.setTextAppearance(R.style.TextAppearance_Smeem_Body4_regular)
        this.setTextColor(resources.getColor(R.color.gray_400, null))
    }

    fun TextView.switchOn() {
        when (Day.from(this.text.toString())) {
            Day.MON -> this.setBackgroundResource(R.drawable.shape_time_day_active_left)
            Day.SUN -> this.setBackgroundResource(R.drawable.shape_time_day_active_right)
            else -> this.setBackgroundColor(resources.getColor(R.color.point, null))
        }
        this.setTextAppearance(R.style.TextAppearance_Smeem_Body2_semibold)
        this.setTextColor(resources.getColor(R.color.white, null))
    }
}