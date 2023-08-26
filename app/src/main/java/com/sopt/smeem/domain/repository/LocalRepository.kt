package com.sopt.smeem.domain.repository

import com.sopt.smeem.LocalStatus
import com.sopt.smeem.domain.model.Authentication

interface LocalRepository {
    suspend fun getAuthentication(): Authentication
    suspend fun setAuthentication(authentication: Authentication)
    suspend fun isAuthenticated(): Boolean

    suspend fun saveStatus(localStatus: LocalStatus)

    suspend fun checkStatus(localStatus: LocalStatus): Boolean
    suspend fun clear()
}