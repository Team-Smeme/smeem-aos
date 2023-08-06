package com.sopt.smeem.presentation.join

import android.content.Intent
import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityJoinAgreementBinding
import com.sopt.smeem.description
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.home.HomeActivity
import com.sopt.smeem.presentation.join.JoinConstant.NICKNAME
import com.sopt.smeem.util.ButtonUtil.switchOff
import com.sopt.smeem.util.ButtonUtil.switchOn
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class JoinWithAgreementActivity :
    BindingActivity<ActivityJoinAgreementBinding>(R.layout.activity_join_agreement) {
    private var elements: Map<EntranceSelection, MaterialButton>? = null
    private val vm: JoinVM by viewModels()

    override fun constructLayout() {
        EntranceSelection.SERVICE.id = binding.btnEntranceAgreementService.id
        EntranceSelection.PERSONAL.id = binding.btnEntranceAgreementPersonal.id
        EntranceSelection.LOCATION.id = binding.btnEntranceAgreementLocation.id
        EntranceSelection.MARKETING.id = binding.btnEntranceAgreementMarketing.id

        elements = mapOf(
            EntranceSelection.SERVICE to binding.btnEntranceAgreementService,
            EntranceSelection.PERSONAL to binding.btnEntranceAgreementPersonal,
            EntranceSelection.LOCATION to binding.btnEntranceAgreementLocation,
            EntranceSelection.MARKETING to binding.btnEntranceAgreementMarketing,
        )
    }

    override fun addListeners() {
        onTouchAllSelection()
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
                binding.btnEntranceNext.isEnabled = false
            } else {
                binding.btnEntranceAgreementAll.switchOn()
                vm.selectAll()
                elements?.values?.forEach { element ->
                    element.switchOnWithoutContent()
                }
                binding.btnEntranceNext.isEnabled = true
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
                binding.btnEntranceNext.isEnabled = vm.canGoNext()
            }
        }
    }

    private fun onTouchNext() {
        binding.btnEntranceNext.setOnSingleClickListener {
            val nickname =
                intent.getStringExtra(NICKNAME) ?: throw IllegalStateException("알 수 없는 에러가 발생했습니다.")
            val selected = vm.getSelected()
            sendServer(nickname, selected)
            afterJoinSuccess()
        }
    }

    private fun sendServer(nickname: String, selected: Set<EntranceSelection>) {
        vm.registerNicknameAndAcceptance(
            nickname,
            selected,
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
                    startActivity(Intent(this, HomeActivity::class.java))
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
}


internal fun MaterialButton.switchOffWithoutContent() {
    this.setIconResource(R.drawable.ic_selection_inactive)
}