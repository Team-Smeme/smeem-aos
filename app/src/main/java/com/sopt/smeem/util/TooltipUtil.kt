package com.sopt.smeem.util

import android.content.Context
import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.IconGravity
import com.skydoves.balloon.showAlignTop
import com.sopt.smeem.R

object TooltipUtil {
    // 랜덤 주제 툴팁
    fun View.createTopicTooltip(context: Context, owner: LifecycleOwner?) {
        val tooltip = Balloon.Builder(context)
            .setWidth(BalloonSizeSpec.WRAP)
            .setHeight(BalloonSizeSpec.WRAP)
            .setText("랜덤 주제를 받아 보세요!")
            .setTextColorResource(R.color.white)
            .setTextSize(15f)
            .setMarginBottom(4)
            .setMarginRight(16)
            .setElevation(0)
            .setIconDrawableResource(R.drawable.ic_tooltip_close)
            .setIconSize(12)
            .setIconGravity(IconGravity.END)
            .setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
            .setArrowSize(10)
            .setArrowPosition(0.5f)
            .setPaddingHorizontal(16)
            .setPaddingVertical(11)
            .setCornerRadius(6f)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .setBackgroundColorResource(R.color.point)
            .setLifecycleOwner(owner)
            .setDismissWhenTouchOutside(false)
            .build()
        this.showAlignTop(tooltip)
        tooltip.dismissTooltip()
    }

    // 툴팁 클릭 시 제거
    private fun Balloon.dismissTooltip() {
        this.setOnBalloonClickListener {
            this.dismiss()
        }
    }
}