package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.DiaryWriter
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.repository.DiaryRepository

class DiaryRepositoryImpl(
    private val diaryWriter: DiaryWriter
) : DiaryRepository {
    override suspend fun postDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryWriter.writeDiary(diary) }

    override suspend fun patchDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryWriter.editDiary(diary) }
}