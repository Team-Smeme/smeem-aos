package com.sopt.smeem.presentation.mypage

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.model.Badge
import com.sopt.smeem.domain.model.BadgeType
import com.sopt.smeem.domain.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BadgeViewModel @Inject constructor(
    private val userRepository: UserRepository,
) : ViewModel() {

    private val _badges: MutableLiveData<Map<BadgeType, List<Badge>>> = MutableLiveData()
    val badges: LiveData<Map<BadgeType, List<Badge>>>
        get() = _badges

    fun getBadges(onError: (Throwable) -> Unit) {
        viewModelScope.launch {
            try {
                userRepository.getMyBadges().run {
                    data().let { dto ->
                        // FIXME : 구현시 주입
                        // _badges.value =
                    }
                }
            } catch (t: Throwable) {
                onError(t)
            }
        }
    }
}
