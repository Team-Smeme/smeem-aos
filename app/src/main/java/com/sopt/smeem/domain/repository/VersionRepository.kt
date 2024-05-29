package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.common.ApiResult
import com.sopt.smeem.domain.dto.VersionDto

interface VersionRepository {
    suspend fun getVersion(): ApiResult<VersionDto>
}