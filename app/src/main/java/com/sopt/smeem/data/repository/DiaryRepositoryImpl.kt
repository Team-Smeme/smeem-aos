package com.sopt.smeem.data.repository

import com.sopt.smeem.data.datasource.DiaryReader
import com.sopt.smeem.data.model.request.DiaryRequest
import com.sopt.smeem.data.service.DiaryService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.DeleteDiaryRequestDto
import com.sopt.smeem.domain.dto.GetDiaryResponseDto
import com.sopt.smeem.domain.dto.GetDiarySummariesDto
import com.sopt.smeem.domain.dto.GetDiarySummaryDto
import com.sopt.smeem.domain.dto.GetTopicDto
import com.sopt.smeem.domain.dto.PatchDiaryRequestDto
import com.sopt.smeem.domain.dto.RetrievedBadgeDto
import com.sopt.smeem.domain.dto.WriteDiaryRequestDto
import com.sopt.smeem.domain.dto.WriteDiaryResponseDto
import com.sopt.smeem.domain.repository.DiaryRepository
import com.sopt.smeem.util.DateUtil

class DiaryRepositoryImpl(
    private val diaryService: DiaryService,
    private val diaryReader: DiaryReader,
) : DiaryRepository {
    override suspend fun postDiary(diary: WriteDiaryRequestDto): ApiResult<WriteDiaryResponseDto> =
        diaryService.post(DiaryRequest.Writing(diary.content, diary.topicId)).let { response ->
            if (response.isSuccessful) {
                response.body()!!.let {
                    ApiResult(
                        response.code(),
                        WriteDiaryResponseDto(
                            diaryId = it.data.diaryId,
                            retrievedBadgeList = it.data.badges.map { badge ->
                                RetrievedBadgeDto(
                                    badge.name,
                                    badge.imageUrl
                                )
                            }
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun patchDiary(dto: PatchDiaryRequestDto): ApiResult<Unit> =
        diaryService.patch(DiaryRequest.Editing(dto.content), dto.id).let { response ->
            if (response.isSuccessful) {
                response.body()!!.let { ApiResult(response.code(), Unit) }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun deleteDiary(dto: DeleteDiaryRequestDto): ApiResult<Unit> =
        diaryService.delete(dto.id).let { response ->
            if (response.isSuccessful) {
                response.body()!!.let { ApiResult(response.code(), Unit) }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun getDiaryDetail(diaryId: Long): ApiResult<GetDiaryResponseDto> =
        diaryService.getDetail(diaryId).let { response ->
            if (response.isSuccessful) {
                response.body()!!.let { body ->
                    ApiResult(
                        response.code(), GetDiaryResponseDto(
                            id = body.data.diaryId,
                            content = body.data.content,
                            createdAt = DateUtil.asLocalDateTime(body.data.createdAt),
                            username = body.data.username,
                            topic = body.data.topic,
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    // calendar related
    override suspend fun getDiaries(
        start: String?,
        end: String?
    ): ApiResult<GetDiarySummariesDto> =
        diaryReader.getList(start, end).let { response ->
            if (response.isSuccessful) {
                response.body()!!.data.let { data ->
                    ApiResult(
                        response.code(), GetDiarySummariesDto(
                            diaries = data.diaries.associate {
                                DateUtil.asLocalDateTime(it.createdAt)
                                    .toLocalDate() to GetDiarySummaryDto(
                                    id = it.diaryId,
                                    content = it.content,
                                    createdAt = DateUtil.asLocalDateTime(it.createdAt)
                                        .toLocalTime()
                                )
                            },
                            has30Past = data.has30Past,
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }

    override suspend fun getTopic(): ApiResult<GetTopicDto> =
        diaryService.getTopic().let { response ->
            if (response.isSuccessful) {
                response.body()!!.data.let { data ->
                    ApiResult(
                        response.code(), GetTopicDto(
                            id = data.topicId,
                            content = data.content
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }
}
