package com.sopt.smeem.presentation.mypage

import android.content.Intent
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.R
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.databinding.ActivityChangingNicknameBinding
import com.sopt.smeem.domain.repository.LoginRepository
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.presentation.BindingActivity
import com.sopt.smeem.util.setOnSingleClickListener
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class ChangingNicknameActivity :
    BindingActivity<ActivityChangingNicknameBinding>(R.layout.activity_changing_nickname) {
    private val vm: ChangingNicknameVM by viewModels()


    override fun addListeners() {
        onTextWrite()
        onTouchCompleted()
        onTouchBack()
    }

    private fun onTextWrite() {
        binding.etChangeNickname.addTextChangedListener { watcher ->
            if (watcher!!.length <= 0 || watcher!!.length > 16) {
                nextButtonOff()
            } else {
                nextButtonOn()
            }
        }
    }

    private fun onTouchBack() {
        binding.topbarChangeNickname.setNavigationOnClickListener {
            goBack()
        }
    }

    private fun goBack() {
        startActivity(Intent(this, MyPageActivity::class.java))
        finish()
    }

    private fun onTouchCompleted() {
        binding.btnChangeNicknameNext.setOnSingleClickListener {
            vm.content = binding.etChangeNickname.text.toString()
            checkNicknameDuplicated()
            afterCheckNicknameDuplicated()
        }
    }

    private fun nextButtonOff() {
        binding.btnChangeNicknameNext.setBackgroundColor(
            resources.getColor(
                R.color.point_inactive,
                null
            )
        )
        binding.btnChangeNicknameNext.isEnabled = false
    }

    private fun nextButtonOn() {
        binding.btnChangeNicknameNext.setBackgroundColor(resources.getColor(R.color.point, null))
        binding.btnChangeNicknameNext.isEnabled = true
    }

    private fun checkNicknameDuplicated() {
        vm.callApiNicknameDuplicated(
            onError = { t -> Toast.makeText(this, t.message, Toast.LENGTH_SHORT).show() }
        )
    }

    private fun afterCheckNicknameDuplicated() {
        vm.nicknameDuplicated.observe(this@ChangingNicknameActivity) { isDuplicated ->
            when (isDuplicated) {
                true -> {
                    binding.tvChangeNicknameDuplicated.visibility = View.VISIBLE
                    binding.btnChangeNicknameNext.isEnabled = false
                }

                false -> {
                    vm.send(binding.etChangeNickname.text.toString())
                    goBack()
                }
            }
        }
    }
}

@HiltViewModel
class ChangingNicknameVM @Inject constructor(
    private val userRepository: UserRepository,
    @Anonymous private val loginRepository: LoginRepository,
) : ViewModel() {
    var content: String = ""
    private val _nicknameDuplicated = MutableLiveData<Boolean>()
    val nicknameDuplicated: LiveData<Boolean>
        get() = _nicknameDuplicated

    fun send(name: String): Unit //  TODO : nickname 변경 api call
    {
        viewModelScope.launch {
            userRepository.modifyUserInfo(
                nickname = name
            )
        }
    }

    fun callApiNicknameDuplicated(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            loginRepository.checkNicknameDuplicated(content)
                .onSuccess { result ->
                    _nicknameDuplicated.value = result
                }
                .onHttpFailure { onError(it) }
        }
    }
}