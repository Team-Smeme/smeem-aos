package com.sopt.smeem.data.repository

import com.sopt.smeem.data.service.BookmarkService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.BookmarkDto
import com.sopt.smeem.domain.dto.GetBookmarksResponseDto
import com.sopt.smeem.domain.repository.BookmarkRepository
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val bookmarkService: BookmarkService
) : BookmarkRepository {
    
    override suspend fun getBookmarks(): ApiResult<GetBookmarksResponseDto> =
        bookmarkService.getBookmarks().let { response ->
            if (response.isSuccessful) {
                response.body()!!.let { apiResponse ->
                    ApiResult(
                        statusCode = response.code(),
                        data = GetBookmarksResponseDto(
                            bookmarks = apiResponse.data.bookmarks?.mapNotNull { item ->
                                // bookmarkId가 null이면 해당 아이템을 제외
                                val bookmarkId = item.bookmarkId ?: return@mapNotNull null
                                
                                BookmarkDto(
                                    bookmarkId = bookmarkId,
                                    thumbnailImageUrl = item.thumbnailImageUrl ?: "",
                                    expression = item.expression ?: "표현식 없음",
                                    description = item.description ?: "",
                                    createdAt = item.createdAt ?: "",
                                    scrapType = item.scrapType ?: "P"
                                )
                            } ?: emptyList()
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }
}