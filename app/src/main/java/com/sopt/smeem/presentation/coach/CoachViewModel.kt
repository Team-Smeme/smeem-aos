package com.sopt.smeem.presentation.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.presentation.detail.DiaryDetail
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.postSideEffect
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ContainerHost<CoachState, CoachSideEffect>, ViewModel() {
    override val container: Container<CoachState, CoachSideEffect> = container(CoachState())

    fun initialize(diaryId: Long, initialContent: String) {
        intent {
            reduce {
                state.copy(
                    diaryId = diaryId,
                    initialDiaryContent = initialContent,
                    isLoading = true
                )
            }
        }
        getDiaryDetail()
    }

    private fun getDiaryDetail() {
        viewModelScope.launch {
            try {
                intent {
                    diaryRepository.getDiaryDetail(state.diaryId).run {
                        data().let { dto ->
                            intent {
                                reduce {
                                    state.copy(
                                        diaryDetail = DiaryDetail.from(dto),
                                        isLoading = false
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                intent {
                    reduce {
                        state.copy(
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun onCoachClick() {
        intent {
            postSideEffect(CoachSideEffect.NavigateToCoachDetail)
        }
        getCorrections()
    }

    private fun getCorrections() {
        viewModelScope.launch {
            intent { reduce { state.copy(isLoading = true) } }

            try {
                intent {
                    val result =
                        diaryRepository.getCorrections(state.diaryId).data().toPersistentList()
                    reduce { state.copy(corrections = result, isLoading = false) }
                }
            } catch (t: Throwable) {
                intent { reduce { state.copy(isLoading = false) } }
            }
        }
    }
}
