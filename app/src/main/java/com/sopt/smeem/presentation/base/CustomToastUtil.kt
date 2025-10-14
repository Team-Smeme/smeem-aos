package com.sopt.smeem.presentation.base

import android.app.Activity
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay

class CustomToastUtil private constructor() {
    companion object {
        private var currentToastView: ComposeView? = null

        fun showToast(activity: Activity, message: ToastMessage, duration: Long = 3000L) {
            hideCurrentToast()

            val rootView = activity.findViewById<ViewGroup>(android.R.id.content)

            val composeView = ComposeView(activity).apply {
                setContent {
                    ToastWithAnimation(
                        message = message,
                        duration = duration,
                        onDismiss = {
                            hideCurrentToast()
                        }
                    )
                }
            }

            val layoutParams = FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT,
                FrameLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                gravity = Gravity.BOTTOM
                setMargins(0, 0, 0, 100) // 하단에서 100dp 위쪽에 표시
            }

            rootView.addView(composeView, layoutParams)
            currentToastView = composeView
        }


        private fun hideCurrentToast() {
            currentToastView?.let { view ->
                (view.parent as? ViewGroup)?.removeView(view)
                currentToastView = null
            }
        }
    }
}

@Composable
private fun ToastWithAnimation(
    message: ToastMessage,
    duration: Long,
    onDismiss: () -> Unit
) {
    var isVisible by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        isVisible = true
        delay(duration)
        isVisible = false
        delay(300) // 애니메이션 완료 대기
        onDismiss()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        AnimatedVisibility(
            visible = isVisible,
            enter = slideInVertically(
                initialOffsetY = { it / 2 }
            ) + fadeIn(),
            exit = slideOutVertically(
                targetOffsetY = { it / 2 }
            ) + fadeOut()
        ) {
            CustomToast(
                message = message,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }
    }
}

// Activity 확장 함수
fun Activity.showCustomToast(title: String, subtitle: String) {
    val message = ToastMessage(title, subtitle)
    CustomToastUtil.showToast(this, message)
}