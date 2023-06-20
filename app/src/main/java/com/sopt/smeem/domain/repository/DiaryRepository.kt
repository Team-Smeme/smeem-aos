package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Diary

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
    suspend fun removeDiary(diary: Diary) : Result<Unit>
}