package com.sopt.smeem.presentation.coach

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sopt.smeem.domain.common.SmeemErrorCode
import com.sopt.smeem.domain.common.SmeemException
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import org.orbitmvi.orbit.Container
import org.orbitmvi.orbit.ContainerHost
import org.orbitmvi.orbit.syntax.simple.intent
import org.orbitmvi.orbit.syntax.simple.reduce
import org.orbitmvi.orbit.viewmodel.container
import javax.inject.Inject

@HiltViewModel
class CoachViewModel @Inject constructor(
    private val diaryRepository: DiaryRepository
) : ContainerHost<CoachState, CoachSideEffect>, ViewModel() {
    override val container: Container<CoachState, CoachSideEffect> = container(CoachState())

    fun setDiaryId(diaryId: Long) {
        if (diaryId < 0) {
            throw SmeemException(SmeemErrorCode.SYSTEM_ERROR, "잘못된 diaryId ($diaryId) 입니다.")
        }
        intent {
            reduce {
                state.copy(diaryId = diaryId)
            }
        }
    }

    fun getDiaryDetail() {
        viewModelScope.launch {
            try {
                intent {
                    diaryRepository.getDiaryDetail(state.diaryId).run {
                        data().let { dto ->
                            intent {
                                reduce {
                                    state.copy(
                                        diaryContent = dto.content,
                                        createdAt = DateUtil.asString(dto.createdAt),
                                        writerUsername = dto.username
                                    )
                                }
                            }
                        }
                    }
                }
            } catch (t: Throwable) {
                intent {
                    reduce {
                        state.copy(diaryContent = "일기를 불러오는데 실패했습니다.")
                    }
                }
            }
        }
    }
}
