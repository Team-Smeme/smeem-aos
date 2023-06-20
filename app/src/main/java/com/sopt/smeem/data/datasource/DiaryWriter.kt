package com.sopt.smeem.data.datasource

import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.model.response.ApiResponse
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.domain.model.Diary

class DiaryWriter(
    private val diaryService: DiaryService
) {
    suspend fun writeDiary(diary: Diary): ApiResponse<Unit> = diaryService.post(
        request = DiaryRequest.Writing(
            content = diary.content,
            topicId = diary.topicId
        )
    )

    fun editDiary(diary: Diary): ApiResponse<Unit> = diaryService.patch(
        request = DiaryRequest.Editing(
            content = diary.content,
        ),
        diaryId = diary.id!!
    )

}