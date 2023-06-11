package com.sopt.smeem.util

import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R

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
}