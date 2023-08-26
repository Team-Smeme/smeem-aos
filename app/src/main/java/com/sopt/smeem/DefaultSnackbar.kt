package com.sopt.smeem

import android.view.LayoutInflater
import android.view.View
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.snackbar.Snackbar
import com.sopt.smeem.databinding.SnackbarDefaultBinding

class DefaultSnackBar(view: View, private val anchor: Int?, private val message: String) {

    companion object {
        fun make(view: View, message: String) = DefaultSnackBar(view, null, message)
        fun makeOnTopOf(view: View, anchor: Int, message: String) = DefaultSnackBar(view, anchor, message)
    }

    private val context = view.context
    private val snackbar = if (anchor == null) {
        Snackbar.make(view, "", Snackbar.LENGTH_SHORT)
    } else {
        Snackbar.make(view, "", Snackbar.LENGTH_SHORT).setAnchorView(anchor)
    }
    private val snackbarLayout = snackbar.view as Snackbar.SnackbarLayout

    private val inflater = LayoutInflater.from(context)
    private val snackbarBinding: SnackbarDefaultBinding =
        DataBindingUtil.inflate(inflater, R.layout.snackbar_default, null, false)

    init {
        initView()
        initData()
    }

    private fun initView() {
        with(snackbarLayout) {
            removeAllViews()
            setPadding(0, 0, 0, 0)
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
            addView(snackbarBinding.root, 0)
        }
    }

    private fun initData() {
        snackbarBinding.tvSnackbarDefault.text = message
    }

    fun show() {
        snackbar.show()
    }
}