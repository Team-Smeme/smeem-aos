package com.sopt.smeem.presentation.auth.entrance

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.Authenticated
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.ApiPool.onHttpFailure
import com.sopt.smeem.domain.repository.UserRepository
import com.sopt.smeem.presentation.auth.entrance.EntranceSelection.LOCATION
import com.sopt.smeem.presentation.auth.entrance.EntranceSelection.MARKETING
import com.sopt.smeem.presentation.auth.entrance.EntranceSelection.PERSONAL
import com.sopt.smeem.presentation.auth.entrance.EntranceSelection.SERVICE
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EntranceAgreementVM @Inject constructor() : ViewModel() {
    @Inject
    @Authenticated
    lateinit var userRepository: UserRepository

    private val selections = mutableSetOf<EntranceSelection>()
    fun selectAll() {
        selections.addAll(EntranceSelection.getAll())
    }

    fun cancelAll() {
        selections.clear()
    }

    fun cancel(selection: EntranceSelection) {
        selections.remove(selection)
    }

    fun add(selection: EntranceSelection) {
        selections.add(selection)
    }

    fun isSelected(element: EntranceSelection) = selections.contains(element)

    fun allSelected() = selections.size == EntranceSelection.getAll().size
    fun canGoNext() = selections.containsAll(setOf(SERVICE, PERSONAL, LOCATION))
    fun getSelected() = selections
    fun registerNicknameAndAcceptance(
        nickname: String,
        selected: Set<EntranceSelection>,
        onSuccess: (Unit) -> Unit,
        onError: (SmeemException) -> Unit
    ) {
        viewModelScope.launch {
            userRepository.patchNicknameAndAcceptance(nickname, selected.contains(MARKETING))
                .onSuccess { onSuccess }
                .onHttpFailure { e -> onError(e) }
        }
    }
}

enum class EntranceSelection {
    SERVICE,
    PERSONAL,
    LOCATION,
    MARKETING,
    ;

    var id: Int = 0
    var selected: Boolean = false

    companion object {
        fun getAll(): Set<EntranceSelection> = setOf(SERVICE, PERSONAL, LOCATION, MARKETING)
        fun findById(id: Int) = getAll().find { it.id == id } ?: throw IllegalStateException()
    }
}