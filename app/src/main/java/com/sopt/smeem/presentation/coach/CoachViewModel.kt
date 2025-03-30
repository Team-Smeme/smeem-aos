package com.sopt.smeem.presentation.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.dto.SurveyRequestDto
import com.sopt.smeem.domain.model.SurveyType
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.domain.repository.UserRepository
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
    private val diaryRepository: DiaryRepository,
    private val userRepository: UserRepository,
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
                    val response = diaryRepository.getCorrections(state.diaryId).data()

                    reduce {
                        state.copy(
                            corrections = response.corrections.take(10).toPersistentList(),
                            username = response.username,
                            totalCount = response.totalCount,
                            isLoading = false
                        )
                    }
                }
            } catch (t: Throwable) {
                intent {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(CoachSideEffect.ShowError(t.message.toString()))
                }
            }
        }
    }

    fun onThumbSelected(selection: ThumbSelection) {
        intent {
            if (state.thumbSelection == selection) return@intent

            reduce { state.copy(thumbSelection = selection, surveyReason = "") }
        }
    }

    fun onSurveyTypeSelected(surveyType: SurveyType) {
        intent {
            val currentSelectedTypes = state.selectedSurveyTypes
            val updatedSelectedTypes = if (currentSelectedTypes.contains(surveyType)) {
                // 이미 선택되어 있으면 제거 (선택 해제)
                currentSelectedTypes.minus(surveyType)
            } else {
                // 선택되어 있지 않으면 추가
                currentSelectedTypes.plus(surveyType)
            }

            reduce { state.copy(selectedSurveyTypes = updatedSelectedTypes) }
        }
    }

    fun postSurvey() {
        viewModelScope.launch {
            intent { reduce { state.copy(isLoading = true) } }
            try {
                intent {
                    val surveyRequest = SurveyRequestDto(
                        diaryId = state.diaryId,
                        isSatisfied = state.thumbSelection == ThumbSelection.THUMB_UP,
                        dissatisfactionTypes = if (state.thumbSelection == ThumbSelection.THUMB_DOWN) {
                            state.selectedSurveyTypes.toList()
                        } else {
                            emptyList()
                        },
                        reason = state.surveyReason
                    )

                    userRepository.postSurvey(surveyRequest)

                    reduce { state.copy(isLoading = false) }
                    postSideEffect(CoachSideEffect.NavigateToHome)
                }
            } catch (t: Throwable) {
                intent {
                    reduce { state.copy(isLoading = false) }
                    postSideEffect(CoachSideEffect.ShowError(t.message.toString()))
                }
            }
        }
    }

    fun updateSurveyReason(reason: String) {
        intent { reduce { state.copy(surveyReason = reason) } }
    }
}
