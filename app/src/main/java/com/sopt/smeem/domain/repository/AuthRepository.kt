package com.sopt.smeem.domain.repository

import com.sopt.smeem.domain.model.auth.Authentication

interface AuthRepository {
    suspend fun getAuthentication(): Authentication?
    suspend fun setAuthentication(authentication: Authentication): Unit
    suspend fun isAuthenticated(): Boolean
}