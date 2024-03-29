package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.DiaryCommander
import com.sopt.smeem.data.datasource.DiaryReader
import com.sopt.smeem.domain.model.Diary
import com.sopt.smeem.domain.model.DiarySummaries
import com.sopt.smeem.domain.model.DiarySummary
import com.sopt.smeem.domain.model.RetrievedBadge
import com.sopt.smeem.domain.model.Topic
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil

class DiaryRepositoryImpl(
    private val diaryCommander: DiaryCommander,
    private val diaryReader: DiaryReader,
) : DiaryRepository {
    override suspend fun postDiary(diary: Diary): Result<List<RetrievedBadge>> =
        kotlin.runCatching { diaryCommander.writeDiary(diary).data!!.badges }
            .map { badges ->
                badges.map { badge ->
                    RetrievedBadge(
                        name = badge.name,
                        imageUrl = badge.imageUrl,
                    )
                }
            }

    override suspend fun patchDiary(diary: Diary): Result<Unit> =
        kotlin.runCatching { diaryCommander.editDiary(diary) }

    override suspend fun removeDiary(diaryId: Long): Result<Unit> =
        kotlin.runCatching { diaryCommander.removeDiary(diaryId) }

    override suspend fun getDiaryDetail(diaryId: Long): Result<Diary> =
        kotlin.runCatching { diaryReader.getDetail(diaryId) }
            .map { response ->
                Diary(
                    id = response.data!!.diaryId,
                    content = response.data.content,
                    topic = response.data.topic,
                    createdAt = response.data.createdAt,
                    username = response.data.username,
                    corrections = response.data.corrections?.map {
                        Diary.Correction(
                            id = it.correctionId,
                            before = it.before,
                            after = it.after,
                        )
                    } ?: emptyList(),
                )
            }

    // calendar related
    override suspend fun getDiaries(
        start: String?,
        end: String?
    ): Result<DiarySummaries> =
        kotlin.runCatching { diaryReader.getList(start, end) }
            .map { response ->
                DiarySummaries(
                    diaries = response.data!!.diaries.associateBy(
                        keySelector = { diary ->
                            DateUtil.asLocalDateTime(diary.createdAt).toLocalDate()
                        },
                        valueTransform = { diary ->
                            DiarySummary(
                                id = diary.diaryId,
                                content = diary.content,
                                createdAt = DateUtil.asLocalDateTime(diary.createdAt).toLocalTime()
                            )
                        }
                    ),
                    has30Past = response.data.has30Past,
                )
            }

    override suspend fun getTopic(): Result<Topic> =
        kotlin.runCatching {
            diaryReader.getTopic()
        }.map { response ->
            Topic(
                id = response.data!!.topicId,
                content = response.data.content,
            )
        }
}
