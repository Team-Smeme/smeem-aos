package com.sopt.smeem.presentation.splash

import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowInsetsControllerCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.BuildConfig
import com.sopt.smeem.R
import com.sopt.smeem.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import kotlin.system.exitProcess

@AndroidEntryPoint
class SplashStartActivity() : AppCompatActivity() {
    private val vm: SplashVM by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        setContentView(R.layout.activity_splash_start)

        constructLayout()
    }

    fun constructLayout() {
        setStatusBarColor()
        setNavigationBarColor()
        checkVersion()
        vm.checkAuthed()
    }

    private fun setStatusBarColor() {
        window.apply {
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = getColor(R.color.point)
            WindowInsetsControllerCompat(
                this,
                this.decorView
            ).isAppearanceLightStatusBars = false
        }
    }

    private fun setNavigationBarColor() {
        window.navigationBarColor = getColor(R.color.point)
    }

    private fun checkVersion() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                vm.version
                    .filter { it.isNotEmpty() }
                    .collectLatest { latestVersion ->
                        val installedVersion = BuildConfig.VERSION_NAME

                        val isNewVersion = when {
                            latestVersion.isEmpty() -> false
                            else -> {
                                val installedVersionX = installedVersion.split(".").first().toInt()
                                val latestVersionX = latestVersion.split(".").first().toInt()

                                installedVersionX != latestVersionX
                            }
                        }

                        if (!isNewVersion) showUpdateDialog() else observeAuthed()
                    }
            }
        }
    }

    private fun showUpdateDialog() {
        MaterialAlertDialogBuilder(this)
            .setCustomTitle(layoutInflater.inflate(R.layout.update_dialog_content, null))
            .setNegativeButton("나가기") { _, _ ->
                finishSmeem()
            }
            .setPositiveButton("업데이트") { dialog, which ->
                val appPackageName = packageName // 현재 앱의 패키지명 가져오기
                try {
                    // Play Store 앱을 사용하여 이동
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("market://details?id=com.sopt.smeem")
                        )
                    )
                } catch (e: ActivityNotFoundException) {
                    // Play Store 앱이 없을 때, 웹 브라우저를 사용하여 이동
                    startActivity(
                        Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://play.google.com/store/apps/details?id=$appPackageName")
                        )
                    )
                }
            }
            .setCancelable(false)
            .show()
    }

    private fun finishSmeem() {
        moveTaskToBack(true)
        finishAndRemoveTask()
        exitProcess(0)
    }

    private fun observeAuthed() {
        vm.isAuthed.observe(this) {
            when (it) {
                true -> { // Home 으로 이동
                    startActivity(Intent(this@SplashStartActivity, HomeActivity::class.java))
                    finish()
                }

                false -> { // Login Splash 로 이동
                    startActivity(Intent(this@SplashStartActivity, SplashLoginActivity::class.java))
                    finish()
                }
            }
        }
    }
}
