package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.Topic

interface DiaryRepository {
    /**
     * 일기 내용을 서버로 전송합니다.
     */
    suspend fun postDiary(diary: Diary): Result<Unit>

    /**
     * 일기 내용을 수정합니다.
     */
    suspend fun patchDiary(diary: Diary): Result<Unit>

    /**
     * 일기를 제거한다.
     */
    suspend fun removeDiary(diary: Diary): Result<Unit>

    /**
     * 일기를 상세 조회한다.
     */
    suspend fun getDiaryDetail(diaryId: Long): Result<Diary>

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
    suspend fun getDiaries(start: String? = null, end: String? = null): Result<DiarySummaries>

    /**
     * 일기 랜덤 주제를 조회합니다.
     */
    suspend fun getTopic(): Result<Topic>
}