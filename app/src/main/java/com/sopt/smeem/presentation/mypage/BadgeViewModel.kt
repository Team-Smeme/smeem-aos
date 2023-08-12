package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Anonymous
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor() : ViewModel() {

    @Inject
    @Anonymous
    lateinit var userRepository: UserRepository

    private val _badges: MutableLiveData<Map<BadgeType, List<Badge>>> = MutableLiveData()
    val badges: LiveData<Map<BadgeType, List<Badge>>>
        get() = _badges

    fun getBadges(
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.getMyBadges()
                .onSuccess {
                    _badges.value = it.groupBy(keySelector = Badge::badgeType)
                }
                .onHttpFailure { e -> onError(e) }
        }
    }
}