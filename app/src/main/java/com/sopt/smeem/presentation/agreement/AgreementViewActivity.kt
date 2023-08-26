package com.sopt.smeem.presentation.agreement

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityAgreementViewBinding
import com.sopt.smeem.presentation.BindingActivity

class AgreementViewActivity : BindingActivity<ActivityAgreementViewBinding>(R.layout.activity_agreement_view) {
    override fun constructLayout() {
        initWebView()
        startWebView()
    }

    private fun initWebView() {
        binding.wvAgreement.settings.apply {
            javaScriptEnabled = true
            loadWithOverviewMode = true
            useWideViewPort = true
        }
    }

    private fun startWebView() {
//        intent.getStringExtra("agreement")?.let {
//            binding.wvAgreement.loadUrl(it)
//        }
        binding.wvAgreement.loadUrl(resources.getString(R.string.personal_info_agreement))
    }
}