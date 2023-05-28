package com.sopt.smeem.presentation.auth.entrance

import android.widget.Toast
import androidx.activity.viewModels
import com.google.android.material.button.MaterialButton
import com.sopt.smeem.R
import com.sopt.smeem.databinding.ActivityEntranceAgreementBinding
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.presentation.auth.entrance.EntranceConstant.NICKNAME
import com.sopt.smeem.presentation.auth.onboarding.switchOff
import com.sopt.smeem.presentation.auth.onboarding.switchOn

class EntranceAgreementActivity :
    BindingActivity<ActivityEntranceAgreementBinding>(R.layout.activity_entrance_agreement) {
    private var elements: Map<EntranceSelection, MaterialButton>? = null
    private val vm: EntranceAgreementVM by viewModels()

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
        binding.btnEntranceAgreementAll.setOnClickListener {

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
        binding.btnEntranceNext.setOnClickListener {
            val nickname =
                intent.getStringExtra(NICKNAME) ?: throw IllegalStateException("알 수 없는 에러가 발생했습니다.")
            val selected = vm.getSelected()

            sendServer(nickname, selected)
        }
    }

    private fun sendServer(nickname: String, selected: Set<EntranceSelection>) {
        // TODO : PATCH 유저정보 설정 API call
        Toast.makeText(this, "닉네임 변경 API 호출", Toast.LENGTH_SHORT).show()
    }
}

internal fun MaterialButton.switchOnWithoutContent() {
    this.setIconResource(R.drawable.ic_selection_active)
    this.setStrokeColorResource(R.color.point)
}


internal fun MaterialButton.switchOffWithoutContent() {
    this.setIconResource(R.drawable.ic_selection_inactive)
}