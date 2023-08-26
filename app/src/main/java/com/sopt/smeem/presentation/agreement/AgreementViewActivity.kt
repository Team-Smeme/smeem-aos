package com.sopt.smeem.presentation.agreement

import android.webkit.WebView
import android.webkit.WebViewClient
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityAgreementViewBinding
import com.sopt.smeem.presentation.BindingActivity

class AgreementViewActivity :
    BindingActivity<ActivityAgreementViewBinding>(R.layout.activity_agreement_view) {
    override fun constructLayout() {
        initWebView()
        startWebView()
    }

    private fun initWebView() {
        binding.wvAgreement.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView, url: String): Boolean {
                view.loadUrl(url)
                return true
            }

            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
            }
        }

        binding.wvAgreement.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
            domStorageEnabled = true
        }
    }

    private fun startWebView() {
        intent.getStringExtra("agreement")?.let {
            binding.wvAgreement.loadUrl(it)
        }
    }
}