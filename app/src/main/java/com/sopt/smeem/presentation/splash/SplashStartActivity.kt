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
import timber.log.Timber
import kotlin.system.exitProcess

@AndroidEntryPoint
class SplashStartActivity : AppCompatActivity() {
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
                    .collectLatest { forceVersion ->

                        // DEBUG 모드일 경우 .dev 제거, 배포 모드에서는 그대로 사용
                        val installedVersion = if (BuildConfig.DEBUG) {
                            BuildConfig.VERSION_NAME.removeSuffix(".dev")
                        } else {
                            BuildConfig.VERSION_NAME
                        }

                        try {
                            val (installedX, installedY, installedZ) = installedVersion.split(".")
                                .map { it.toInt() }
                            val (forceX, forceY, forceZ) = forceVersion.split(".")
                                .map { it.toInt() }

                            val isForceUpdateRequired = when {
                                forceX > installedX -> true // 강제 업데이트 버전의 x가 더 크면 강제 업데이트
                                forceX == installedX && forceY > installedY -> true // x가 같고 y가 크면 강제 업데이트
                                forceX == installedX && forceY == installedY && forceZ > installedZ -> true // x, y가 같고 z가 클 때 강제 업데이트
                                else -> false
                            }

                            if (isForceUpdateRequired) showUpdateDialog() else observeAuthed()
                        } catch (e: Exception) {
                            // 잘못된 버전 형식이 감지되면 로그 출력
                            Timber.e(
                                "VersionCheck",
                                "Invalid version format: $installedVersion or $forceVersion",
                                e
                            )
                        }
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
                            Uri.parse("market://details?id=$appPackageName")
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
