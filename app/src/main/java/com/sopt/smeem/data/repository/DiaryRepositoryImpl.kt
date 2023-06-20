package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.DiaryCommander
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.repository.DiaryRepository

class DiaryRepositoryImpl(
    private val diaryCommander: DiaryCommander,
) : DiaryRepository {
    override suspend fun postDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.writeDiary(diary) }

    override suspend fun patchDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.editDiary(diary) }

    override suspend fun removeDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.removeDiary(diary) }
}