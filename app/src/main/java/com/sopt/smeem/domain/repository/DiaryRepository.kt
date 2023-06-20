package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Diary

interface DiaryRepository {
    /**
     * 일기 내용을 서버로 전송합니다.
     */
    suspend fun postDiary(diary: Diary): Result<Unit>


}