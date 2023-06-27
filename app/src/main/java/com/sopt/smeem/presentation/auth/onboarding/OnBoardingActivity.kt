package com.sopt.smeem.presentation.auth.onboarding

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.databinding.ActivityOnBoardingBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.auth.entrance.EntranceNicknameActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class OnBoardingActivity :
    BindingActivity<ActivityOnBoardingBinding>(R.layout.activity_on_boarding) {
    private val vm: OnBoardingVM by viewModels()
    lateinit var bs: SignUpBottomSheet

    override fun constructLayout() {
        super.constructLayout()
        setUpFragments()
        setUpBottomSheet()
    }

    override fun addListeners() {
        onNext()
        onSetTimeLater()
    }

    override fun addObservers() {
        onNextChanged()
        doAfterLoginSuccess()
        goAnonymous()
        alreadyHasToken() // 3/3 (트레이닝 시간 설정) 에서 로그인 바텀시트 띄우기전에 이미 kakao 로그인이 된 상태인지 확인
    }

    private fun setUpBottomSheet() {
        bs = SignUpBottomSheet()
    }

    private fun setUpFragments() {
        binding.tvOnBoardingHeaderNo.text = "1"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_on_boarding, SettingGoalFragment())
            .commit()
    }

    private fun onNext() {
        binding.btnOnBoardingNext.setOnClickListener {
            if (vm.step == 0) {
                binding.tvOnBoardingHeaderNo.text = "2"
                binding.tvOnBoardingHeaderTitleStatic.text =
                    resources.getText(R.string.on_boarding_encouraging_header_title)
                binding.tvOnBoardingHeaderDescriptionStatic.text =
                    resources.getText(R.string.on_boarding_encouraging_header_description)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fcv_on_boarding,
                        SettingEncouragingFragment()
                    )
                    .commit()
                vm.goToNext()
            } else if (vm.step == 1) {
                binding.btnOnBoardingNext.text = "완료"
                binding.tvOnBoardingHeaderNo.text = "3"
                binding.tvOnBoardingHeaderTitleStatic.text =
                    resources.getText(R.string.on_boarding_time_header_title)
                binding.tvOnBoardingHeaderDescriptionStatic.text =
                    resources.getText(R.string.on_boarding_time_header_description)
                supportFragmentManager.beginTransaction()
                    .replace(
                        R.id.fcv_on_boarding,
                        SettingTimeFragment()
                    )
                    .commit()
                vm.goToNext()
            } else if (vm.step == 2) {
                vm.checkToken()
                // 다이얼로그나 바텀시트 이탈했을 때 다시 띄워지지 않는 이슈가 있어 주석처리
//                vm.goToNext()
            }
        }
    }

    private fun onSetTimeLater() {
        vm.setTimeLater.observe(this) {
            if (it) { // true
                bs.show(supportFragmentManager, SignUpBottomSheet.TAG)
            }
        }
    }

    private fun onNextChanged() {
        vm.selectedGoal.observe(
            this@OnBoardingActivity
        ) {
            binding.btnOnBoardingNext.isEnabled = (it != TrainingGoalType.NO_IDEA)
        }
    }

    private fun doAfterLoginSuccess() {
        vm.loginResult.observe(this@OnBoardingActivity) {
            when (it.isRegistered) {
                true -> gotoHome()
                false -> {
                    vm.sendPlanData(
                        onSuccess = {
                            val toEntrance = Intent(
                                this@OnBoardingActivity,
                                EntranceNicknameActivity::class.java
                            )
                            startActivity(toEntrance)

                            if (!isFinishing) finish()
                        },
                        onError = { e ->
                            Toast.makeText(
                                this@OnBoardingActivity,
                                e.description(),
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    )

                }
            }
        }
    }

    private fun goAnonymous() {
        vm.goAnonymous.observe(this@OnBoardingActivity) { wantToBeAnonymous ->
            when (wantToBeAnonymous) {
                true -> {
                    vm.saveOnBoardingData()

                    // TODO : remove
                    Toast.makeText(this, "비회원 기능은 아직 정상 동작하지 않습니다.", Toast.LENGTH_SHORT).show()
                    // gotoHome()
                }

                false -> { // TODO : 어떻게 해야하지? }
                }
            }
        }
    }

    private fun gotoHome() {
        // TODO : HomeActivity 로 이동
    }

    private fun alreadyHasToken() {
        vm.alreadyHasToken.observe(this@OnBoardingActivity) {
            if (it) {
                vm.goToNext()
                vm.sendPlanData(
                    onSuccess = {
                        val toEntrance =
                            Intent(this@OnBoardingActivity, EntranceNicknameActivity::class.java)
                        startActivity(toEntrance)
                        if (!isFinishing) finish()
                    },
                    onError = { e ->
                        Toast.makeText(this@OnBoardingActivity, e.description(), Toast.LENGTH_SHORT)
                            .show()
                    }
                )
            }
            else if (vm.step == 2) {
                // TODO: 알림 권한 다이얼로그를 확인했는지 (예나 아니요 선택) 상태값에 따라 분기처리 - 한번만 띄우기 위해서
                MaterialAlertDialogBuilder(this)
                    .setIcon(R.drawable.ic_notification_dialog)
                    .setTitle("‘smeem’에서 알림을\n" +
                            "보내도록 허용하시겠습니까?")
                    .setNegativeButton("예") { dialog, which ->
                        // TODO: hasAlarm 관련 livedata = true
                        bs.show(supportFragmentManager, SignUpBottomSheet.TAG)
                    }
                    .setPositiveButton("아니요") { dialog, which ->
                        // TODO: hasAlarm 관련 livedata = false
                        bs.show(supportFragmentManager, SignUpBottomSheet.TAG)
                    }
                    .show()
            }
        }
    }
}