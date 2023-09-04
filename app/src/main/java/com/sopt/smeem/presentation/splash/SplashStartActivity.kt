package com.sopt.smeem.presentation.splash

import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.google.firebase.remoteconfig.ktx.remoteConfigSettings
import com.sopt.smeem.BuildConfig
import com.sopt.smeem.R
import com.sopt.smeem.presentation.home.HomeActivity
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

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
        checkVersion()
        vm.checkAuthed()
    }

    private fun setStatusBarColor() {
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = getColor(R.color.point)
    }

    private fun checkVersion() {
        val configSettings = remoteConfigSettings {
            minimumFetchIntervalInSeconds = 3600
        }.toBuilder().build()

        Firebase.remoteConfig.apply {
            setConfigSettingsAsync(configSettings)
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetch().addOnCompleteListener(this@SplashStartActivity) { task ->
                val installedVersion = BuildConfig.VERSION_NAME.replace(".", "@")
                val firebaseVersion =
                    getString("app_version").takeIf { it.isNotEmpty() }?.replace(".", "@")
                val isNewVersion = when {
                    firebaseVersion.isNullOrEmpty() -> false
                    else -> {
                        val installedVersionParts = installedVersion.split("@").map { it.toInt() }
                        val firebaseVersionParts = firebaseVersion.split("@").map { it.toInt() }
                        installedVersionParts.zip(firebaseVersionParts)
                            .all { (installed, firebase) ->
                                installed >= firebase
                            }
                    }
                }

                if (task.isSuccessful) {
                    activate()
                    if (isNewVersion) {
                        Timber.tag("smeem is updated to a new version")
                            .d("%s | %s", installedVersion, firebaseVersion)
                        observeAuthed()
                    } else {
                        Timber.tag("smeem needs to be updated!")
                            .d("%s | %s", installedVersion, firebaseVersion)
                        showUpdateDialog()
                    }
                } else {
                    Timber.e("remote config failed")
                }
            }
        }
    }

    private fun showUpdateDialog() {
        MaterialAlertDialogBuilder(this)
            .setCustomTitle(layoutInflater.inflate(R.layout.update_dialog_content, null))
            .setNegativeButton("나가기") { dialog, which ->
                Timber.d("아니요를 눌렀습니다")
                finishSmeem()
            }
            .setPositiveButton("업데이트") { dialog, which ->
                Timber.d("예를 눌렀습니다")
                // TODO: 스토어로 이동 - 스토어 등록 후 링크 가져오기
                observeAuthed()
            }
            .show()
    }

    private fun finishSmeem() {
        moveTaskToBack(true)
        finishAndRemoveTask()
        System.exit(0)
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
