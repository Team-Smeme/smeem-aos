package com.sopt.smeem.presentation

import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.sopt.smeem.util.hideKeyboard

abstract class BindingActivity<Binding : ViewDataBinding>(
    @LayoutRes private val layoutRes: Int,
) : AppCompatActivity() {
    lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, layoutRes)
        binding.lifecycleOwner = this

        constructLayout()
        addListeners()
        addObservers()
    }

    open fun constructLayout() {}
    open fun addListeners() {}
    open fun addObservers() {}
}
