package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.Authentication

interface LocalRepository {
    suspend fun getAuthentication(): Authentication
    suspend fun setAuthentication(authentication: Authentication)
    suspend fun isAuthenticated(): Boolean

    suspend fun clear()
}