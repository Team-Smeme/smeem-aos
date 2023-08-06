package com.sopt.smeem.presentation.onboarding

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.sopt.smeem.R
import com.sopt.smeem.TrainingGoalType
import com.sopt.smeem.databinding.ActivityOnBoardingBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.join.JoinWithNicknameActivity
import com.sopt.smeem.util.setOnSingleClickListener
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
        onTouchNext()
        onSetTimeLater()
    }

    override fun addObservers() {
        observeStepChanging()
        observeOnStep1()
        observeOnStep3()
    }

    override fun onBackPressed() {
        vm.backStep()
    }

    private fun setUpFragments() {
        binding.tvOnBoardingHeaderNo.text = "1"
        supportFragmentManager.beginTransaction()
            .replace(R.id.fcv_on_boarding, SettingGoalFragment())
            .commit()
    }

    private fun setUpBottomSheet() {
        bs = SignUpBottomSheet()
    }

    private fun onTouchNext() {
        binding.btnOnBoardingNext.setOnSingleClickListener {
            vm.nextStep() // next step 으로 이동
        }
    }

    private fun observeStepChanging() {
        vm.step.observe(this@OnBoardingActivity) { step ->
            when (step) {
                1 -> { // step 1 fragment => 학습 목표 선택하기
                    setHeaderStepNo(1)
                    setHeaderTitle(resources.getText(R.string.on_boarding_goal_header_title))
                    setHeaderDescription(resources.getText(R.string.on_boarding_goal_header_description))

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcv_on_boarding,
                            SettingGoalFragment()
                        )
                        .commit()
                }

                2 -> { // step 2 fragment => 선택한 학습 목표 보여주기
                    setHeaderStepNo(2)
                    setHeaderTitle(resources.getText(R.string.on_boarding_encouraging_header_title))
                    setHeaderDescription(resources.getText(R.string.on_boarding_encouraging_header_description))
                    setButtonTextNext()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcv_on_boarding,
                            DisplayGoalFragment()
                        )
                        .commit()
                }

                3 -> { // step 3 fragment => 알림 시간 설정하기
                    setHeaderStepNo(3)
                    setHeaderTitle(resources.getText(R.string.on_boarding_time_header_title))
                    setHeaderDescription(resources.getText(R.string.on_boarding_time_header_description))
                    setButtonTextComplete()

                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.fcv_on_boarding,
                            SettingTimeFragment()
                        )
                        .commit()
                }

                4 -> { // step 4 : 알림 권한 체크 및 api token 가 local 에 있는지 (이전에 로그인했는지 check)
                    checkPushPermission()
                    checkAlreadyAuthed() // 3/3 (트레이닝 시간 설정) 에서 로그인 바텀시트 띄우기전에 이미 kakao 로그인이 된 상태인지 확인
                }

                else -> {}
            }
        }
    }

    private fun observeOnStep1() {
        vm.selectedGoal.observe(this@OnBoardingActivity) {
            // 어떤 버튼값이라도 선택되어있으면 step2 로가는 next 를 활성화시킨다.
            binding.btnOnBoardingNext.isEnabled = (it != TrainingGoalType.NO_SELECTED)
        }
    }

    private fun observeOnStep3() {
        observeJoinOrAnonymous()
    }

    private fun setHeaderStepNo(no: Int) {
        binding.tvOnBoardingHeaderNo.text = no.toString()
    }

    private fun setHeaderTitle(title: CharSequence) {
        binding.tvOnBoardingHeaderTitleStatic.text = title
    }

    private fun setHeaderDescription(text: CharSequence) {
        binding.tvOnBoardingHeaderDescriptionStatic.text = text
    }

    private fun setButtonTextNext() {
        binding.btnOnBoardingNext.text = "다음"
    }

    private fun setButtonTextComplete() {
        binding.btnOnBoardingNext.text = "완료"
    }

    private fun onSetTimeLater() {
        vm.setTimeLater.observe(this) {
            if (it) { // true
                bs.show(supportFragmentManager, SignUpBottomSheet.TAG)
            }
        }
    }

    private fun observeJoinOrAnonymous() {
        observerToGoAnonymous()
        observerToGoLogin()
    }

    private fun observerToGoAnonymous() {
        vm.goAnonymous.observe(this@OnBoardingActivity) { wantToBeAnonymous ->
            when (wantToBeAnonymous) {
                true -> {
                    vm.saveOnBoardingData()

                    // TODO : remove
                    Toast.makeText(this, "비회원 기능은 아직 동작하지 않습니다.", Toast.LENGTH_SHORT).show()
                    // gotoHome()
                }

                false -> { // TODO : 어떻게 해야하지? }
                }
            }
        }
    }

    private fun observerToGoLogin() {
        vm.loginResult.observe(this@OnBoardingActivity) {
            when (it.isRegistered) {
                true -> gotoHome()
                false -> {
                    vm.sendPlanDataOnAnonymous(
                        onSuccess = {
                            val toJoin = Intent(
                                this@OnBoardingActivity,
                                JoinWithNicknameActivity::class.java
                            )
                            startActivity(toJoin)

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


    private fun gotoHome() {
        startActivity(Intent(this, HomeActivity::class.java))
        finish()
    }

    private fun checkPushPermission() {
        // TODO : push 권한 체크
        MaterialAlertDialogBuilder(this)
            .setIcon(R.drawable.ic_notification_dialog)
            .setTitle(
                "‘smeem’에서 알림을\n" +
                        "보내도록 허용하시겠습니까?"
            )
            .setNegativeButton("예") { dialog, which ->
                // TODO: hasAlarm 관련 livedata = true
            }
            .setPositiveButton("아니요") { dialog, which ->
                // TODO: hasAlarm 관련 livedata = false
            }
            .show()
    }

    private fun checkAlreadyAuthed() {
        if (vm.alreadyAuthed()) {
            vm.sendPlanDataWithAuth(
                onSuccess = {
                    val toEntrance =
                        Intent(this@OnBoardingActivity, JoinWithNicknameActivity::class.java)
                    startActivity(toEntrance)
                    if (!isFinishing) finish()
                },
                onError = { e ->
                    Toast.makeText(this@OnBoardingActivity, e.description(), Toast.LENGTH_SHORT)
                        .show()
                }
            )
        }
        // 사전 로그인이 없었으면 login 동작하도록
        else {
            bs.show(supportFragmentManager, SignUpBottomSheet.TAG)
        }
    }
}