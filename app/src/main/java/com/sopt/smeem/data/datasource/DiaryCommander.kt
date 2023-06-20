package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.domain.model.Diary

class DiaryCommander(
    private val diaryService: DiaryService
) {
    suspend fun writeDiary(diary: Diary): ApiResponse<Unit> = diaryService.post(
        request = DiaryRequest.Writing(
            content = diary.content,
            topicId = diary.topicId
        )
    )

    suspend fun editDiary(diary: Diary): ApiResponse<Unit> = diaryService.patch(
        request = DiaryRequest.Editing(
            content = diary.content,
        ),
        diaryId = diary.id!!
    )

    suspend fun removeDiary(diary: Diary) = diaryService.delete(
        diaryId = diary.id!!
    )

}