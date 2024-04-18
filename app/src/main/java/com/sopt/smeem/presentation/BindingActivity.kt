package com.sopt.smeem.presentation

import android.os.Build
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sopt.smeem.R

abstract class BindingActivity<Binding : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {
    lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createOpenAnimation()
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this

        constructLayout()
        addListeners()
        addObservers()
    }

    override fun finish() {
        super.finish()
        createCloseAnimation()
    }

    private fun createOpenAnimation() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_OPEN, R.anim.fade_in, R.anim.fade_out
            )
        } else {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun createCloseAnimation() {
        if (Build.VERSION.SDK_INT >= 34) {
            overrideActivityTransition(
                OVERRIDE_TRANSITION_CLOSE, R.anim.fade_in, R.anim.fade_out
            )
        } else {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    open fun constructLayout() {}
    open fun addListeners() {}
    open fun addObservers() {}
}
