package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.DiaryCommander
import com.sopt.smeem.data.datasource.DiaryReader
import com.sopt.smeem.domain.model.Date
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.model.Topic
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil

class DiaryRepositoryImpl(
    private val diaryCommander: DiaryCommander,
    private val diaryReader: DiaryReader,
) : DiaryRepository {
    override suspend fun postDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.writeDiary(diary) }

    override suspend fun patchDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.editDiary(diary) }

    override suspend fun removeDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.removeDiary(diary) }

    override suspend fun getDiaryDetail(diaryId: Long): Result<Diary> =
        kotlin.runCatching { diaryReader.getDetail(diaryId) }
            .map { response ->
                Diary(
                    id = response.data!!.diaryId,
                    content = response.data.content,
                    topic = response.data.topic,
                    createdAt = response.data.createdAt,
                    username = response.data.username,
                    corrections = response.data.correctionResponses?.map {
                        Diary.Correction(
                            id = it.correctionId,
                            before = it.before,
                            after = it.after
                        )
                    } ?: emptyList()
                )
            }

    override suspend fun getDiaries(
        start: String?,
        end: String?
    ): Result<DiarySummaries> =
        kotlin.runCatching { diaryReader.getList(start, end) }
            .map { response ->
                DiarySummaries(
                    diaries = response.data!!.diaries.associateBy(
                        keySelector = { diary ->
                            val dateTime = DateUtil.asLocalDateTime(diary.createdAt)
                            Date(
                                year = dateTime.year,
                                month = dateTime.monthValue,
                                day = dateTime.dayOfMonth
                            )
                        },
                        valueTransform = { diary ->
                            DiarySummary(
                                id = diary.diaryId,
                                content = diary.content,
                                createdAt = diary.createdAt
                            )

                        }
                    ),
                    has30Past = response.data.has30Past
                )
            }

    override suspend fun getTopic(): Result<Topic> =
        kotlin.runCatching {
            diaryReader.getTopic()
        }.map { response ->
            Topic(
                id = response.data!!.topicId,
                content = response.data.content
            )
        }
}