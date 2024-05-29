package com.sopt.smeem.data.repository

import com.sopt.smeem.data.service.VersionService
import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.VersionDto
import com.sopt.smeem.domain.repository.VersionRepository
import timber.log.Timber

class VersionRepositoryImpl(
    private val versionService: VersionService,
) : VersionRepository {
    override suspend fun getVersion(): ApiResult<VersionDto> =
        versionService.checkVersion().let { response ->
            if (response.isSuccessful) {
                response.body()!!.data.let { data ->
                    Timber.e("data : $data")
                    ApiResult(
                        response.code(), VersionDto(
                            title = data.title,
                            content = data.content,
                            iosVersion = VersionDto.Version(
                                forceVersion = data.iosVersion.forceVersion,
                                version = data.iosVersion.version
                            ),
                            androidVersion = VersionDto.Version(
                                forceVersion = data.androidVersion.forceVersion,
                                version = data.androidVersion.version
                            )
                        )
                    )
                }
            } else {
                throw response.code().handleStatusCode()
            }
        }
}