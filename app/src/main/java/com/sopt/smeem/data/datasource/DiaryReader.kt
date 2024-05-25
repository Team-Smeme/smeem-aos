package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.model.response.DiaryResponse
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.domain.common.SmeemErrorCode
import com.sopt.smeem.domain.common.SmeemException
import com.sopt.smeem.util.DateUtil
import retrofit2.Response
import java.time.LocalDateTime

class DiaryReader(
    private val diaryService: DiaryService,
) {
    // calendar related
    suspend fun getList(
        start: String?,
        end: String?,
    ): Response<ApiResponse<DiaryResponse.Diaries>> {
        val now = LocalDateTime.now()

        if (start == null || end == null) {
            return diaryService.getList(
                startDate = DateUtil.yyyy_mm_dd(now),
                endDate = DateUtil.yyyy_mm_dd(now),
            )
        }

        if ((DateUtil.gap(start, end) >= 50) || (DateUtil.gap(start, end) < 0)) {
            throw SmeemException(
                errorCode = SmeemErrorCode.CLIENT_ERROR,
            )
        }

        return diaryService.getList(startDate = start, endDate = end)
    }
}
