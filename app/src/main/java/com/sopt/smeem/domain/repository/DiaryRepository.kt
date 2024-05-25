package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.DeleteDiaryRequestDto
import com.sopt.smeem.domain.dto.GetDiaryResponseDto
import com.sopt.smeem.domain.dto.GetDiarySummariesDto
import com.sopt.smeem.domain.dto.GetTopicDto
import com.sopt.smeem.domain.dto.PatchDiaryRequestDto
import com.sopt.smeem.domain.dto.WriteDiaryRequestDto
import com.sopt.smeem.domain.dto.WriteDiaryResponseDto

interface DiaryRepository {
    suspend fun postDiary(dto: WriteDiaryRequestDto): ApiResult<WriteDiaryResponseDto>
    suspend fun patchDiary(dto: PatchDiaryRequestDto): ApiResult<Unit>
    suspend fun deleteDiary(dto: DeleteDiaryRequestDto): ApiResult<Unit>
    suspend fun getDiaryDetail(diaryId: Long): ApiResult<GetDiaryResponseDto>
    suspend fun getTopic(): ApiResult<GetTopicDto>

    // calendar related
    /**
     * 일기 목록을 조회한다.
     * - 각 파라미터는 nullable 하다.
     * - start & end rule
     *   - start <= end 이어야한다.
     *     - 그렇지 않으면 예외발생
     *     - Ex)
     *     start : 2024-06-20, end : 2022-06-20  => 예외 발생
     *
     *   - 둘 중 하나만 날이 기입된 경우, 하루의 결과만 조회한다.
     *     - Ex)
     *     start : 2023-06-20, end : null  => 2023-06-20 조회
     *     start : null, end : 2023-06-20  => 2023-06-20 조회
     *
     *   - 두 날이 같은 경우 해당 날을 조회한다.
     *     - Ex)
     *     start : 2023-06-20, end : 2023-06-20  => 2023-06-20 조회
     *
     *   - 두 날이 모두 Null 인 경우, 그 날짜를 조회한다.
     *     - Ex) Today : 2023-06-20
     *     start: null, end: null  => 2023-06-20 조회
     *
     *   - 두 날짜간의 간격이 50일을 넘어가면 요청을 reject 한다.
     *     - Ex)
     *     start : 2023-04-10, end : 2023-10-20  => 예외 발생
     */
    suspend fun getDiaries(
        start: String? = null,
        end: String? = null
    ): ApiResult<GetDiarySummariesDto>
}