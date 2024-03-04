package com.sopt.smeem.data.datasource

import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.DiaryResponse
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.util.DateUtil
import com.sopt.smeem.util.DateUtil.YYYY_MM_DD
import java.time.LocalDateTime
import java.util.regex.Pattern

class DiaryReader(
    private val diaryService: DiaryService,
) {
    suspend fun getDetail(diaryId: Long): ApiResponse<DiaryResponse.Detail> =
        diaryService.getDetail(diaryId = diaryId)

    // calendar related
    suspend fun getList(
        start: String?,
        end: String?,
    ): ApiResponse<DiaryResponse.Diaries> {
        val now = LocalDateTime.now()

        try {
            if (start == null || end == null) {
                return diaryService.getList(
                    startDate = DateUtil.yyyy_mm_dd(now),
                    endDate = DateUtil.yyyy_mm_dd(now),
                )
            }

            if ((DateUtil.gap(start, end) >= 50) || (DateUtil.gap(start, end) < 0)) {
                throw SmeemException(
                    errorCode = SmeemErrorCode.CLIENT_ERROR,
                    throwable = IllegalArgumentException("날짜에 유호하지 않은 값이 입력되었습니다."),
                )
            }

            return diaryService.getList(startDate = start, endDate = end)
        } catch (t: IllegalArgumentException) {
            throw SmeemException(
                errorCode = SmeemErrorCode.UNKNOWN_ERROR,
                throwable = t,
            )
        }
    }

    suspend fun getTopic(): ApiResponse<DiaryResponse.Topic> = diaryService.getTopic()
}
