package com.sopt.smeem.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.LocalStatus

interface LocalRepository {
    suspend fun setStringValue(key: Preferences.Key<String>, value: String)
    suspend fun remove(key: Preferences.Key<String>)
    suspend fun getAuthentication(): Authentication
    suspend fun setAuthentication(authentication: Authentication)
    suspend fun isAuthenticated(): Boolean

    suspend fun saveStatus(localStatus: LocalStatus, value: Any? = null)

    suspend fun checkStatus(localStatus: LocalStatus): Boolean
    suspend fun clear()
}