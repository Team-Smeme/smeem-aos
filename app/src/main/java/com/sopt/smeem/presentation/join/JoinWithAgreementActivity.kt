package com.sopt.smeem.presentation.join

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.databinding.ActivityJoinAgreementBinding
import com.sopt.smeem.description
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.agreement.AgreementViewActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.join.JoinConstant.ACCESS_TOKEN
import com.sopt.smeem.presentation.join.JoinConstant.NICKNAME
import com.sopt.smeem.presentation.join.JoinConstant.REFRESH_TOKEN
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable


@AndroidEntryPoint
class JoinWithAgreementActivity :
    BindingActivity<ActivityJoinAgreementBinding>(R.layout.activity_join_agreement) {
    private var elements: Map<EntranceSelection, MaterialButton>? = null
    private val vm: JoinVM by viewModels()
    private lateinit var accessToken: String
    private lateinit var refreshToken: String

    override fun constructLayout() {
        accessToken = intent.getStringExtra(ACCESS_TOKEN) ?: throw SmeemException(SmeemErrorCode.SYSTEM_ERROR, "토큰이 정상적으로 전달되지 않았습니다.", IllegalStateException())
        refreshToken = intent.getStringExtra(REFRESH_TOKEN) ?: throw SmeemException(SmeemErrorCode.SYSTEM_ERROR, "토큰이 정상적으로 전달되지 않았습니다.", IllegalStateException())


        EntranceSelection.SERVICE.id = binding.btnEntranceAgreementService.id
        EntranceSelection.PERSONAL.id = binding.btnEntranceAgreementPersonal.id
        EntranceSelection.MARKETING.id = binding.btnEntranceAgreementMarketing.id

        elements = mapOf(
            EntranceSelection.SERVICE to binding.btnEntranceAgreementService,
            EntranceSelection.PERSONAL to binding.btnEntranceAgreementPersonal,
            EntranceSelection.MARKETING to binding.btnEntranceAgreementMarketing,
        )
    }

    override fun addListeners() {
        onTouchAllSelection()
        onTouchDetail()
        onTouchNext()
    }

    private fun onTouchAllSelection() {
        binding.btnEntranceAgreementAll.setOnSingleClickListener {

            if (vm.allSelected()) { // all 이 선택되어있는 경우 (모두 선택된 상태)
                binding.btnEntranceAgreementAll.switchOff()
                elements?.values?.forEach { element ->
                    element.switchOffWithoutContent()
                    vm.cancelAll()
                }
                nextButtonOff()
            } else {
                binding.btnEntranceAgreementAll.switchOn()
                vm.selectAll()
                elements?.values?.forEach { element ->
                    element.switchOnWithoutContent()
                }
                nextButtonOn()
            }
        }

        elements?.values?.forEach { element ->
            element.setOnClickListener {
                val selection = EntranceSelection.findById(element.id)

                if (selection.selected) { // 선택 되어진 상태에서는 해제시켜야함
                    vm.cancel(selection)
                    selection.selected = false
                    element.switchOffWithoutContent()
                    binding.btnEntranceAgreementAll.switchOff()
                } else { // 선택안된 상태에서는 선택해주어야함
                    vm.add(selection)
                    selection.selected = true
                    element.switchOnWithoutContent()
                    if (vm.allSelected()) {
                        binding.btnEntranceAgreementAll.switchOn()
                    }
                }
                if (vm.canGoNext()) nextButtonOn() else nextButtonOff()

            }
        }
    }

    private fun nextButtonOff() {
        with(binding.btnEntranceNext) {
            setBackgroundColor(
                resources.getColor(
                    R.color.point_inactive,
                    null
                )
            )
            isEnabled = false
        }
    }

    private fun nextButtonOn() {
        with(binding.btnEntranceNext) {
            setBackgroundColor(resources.getColor(R.color.point, null))
            isEnabled = true
        }
    }

    private fun onTouchDetail() {
        binding.btnAgreementServiceDetail.setOnSingleClickListener {
            Intent(this, AgreementViewActivity::class.java).apply {
                putExtra("agreement", resources.getString(R.string.service_agreement))
            }.run(::startActivity)
        }
        binding.btnAgreementPersonalDetail.setOnSingleClickListener {
            Intent(this, AgreementViewActivity::class.java).apply {
                putExtra("agreement", resources.getString(R.string.personal_info_agreement))
            }.run(::startActivity)
        }
    }

    private fun onTouchNext() {
        binding.btnEntranceNext.setOnSingleClickListener {
            val nickname =
                intent.getStringExtra(NICKNAME) ?: throw IllegalStateException("알 수 없는 에러가 발생했습니다.")
            val selected = vm.getSelected()
            sendServer(nickname, selected, accessToken)
            afterJoinSuccess()
        }
    }

    private fun sendServer(nickname: String, selected: Set<EntranceSelection>, accessToken: String) {
        vm.registerNicknameAndAcceptance(
            nickname,
            selected,
            accessToken,
            onError = { e ->
                Toast.makeText(this@JoinWithAgreementActivity, e.description(), Toast.LENGTH_SHORT)
                    .show()
            }
        )
    }

    private fun afterJoinSuccess() {
        vm.joinSucceed.observe(this@JoinWithAgreementActivity) {
            when (it) {
                true -> {
                    vm.saveTokenInLocal(accessToken, refreshToken)
                    Intent(this, HomeActivity::class.java).apply {
                        putExtra("retrievedBadge", listOf(
                            RetrievedBadge(
                                "웰컴 배지",
                                "https://github.com/Team-Smeme/Smeme-plan/assets/120551217/6b3319cb-4c6f-4bf2-86dd-7576a44b46c7"
                            )
                        ) as Serializable)
                    }.run(::startActivity)
                    finish()
                }

                false -> {
                    // do nothing
                }
            }

        }
    }
}

internal fun MaterialButton.switchOnWithoutContent() {
    this.setIconResource(R.drawable.ic_selection_active)
    this.setStrokeColorResource(R.color.point)
    this.setTextColor(resources.getColor(R.color.black, null))
}


internal fun MaterialButton.switchOffWithoutContent() {
    this.setIconResource(R.drawable.ic_selection_inactive)
    this.setTextColor(resources.getColor(R.color.gray_600, null))
}