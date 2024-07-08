package com.sopt.smeem.domain.repository

import androidx.datastore.preferences.core.Preferences
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.model.LocalStatus
import kotlinx.coroutines.flow.Flow

interface LocalRepository {
    suspend fun setStringValue(
        key: Preferences.Key<String>,
        value: String,
    )

    suspend fun setBooleanValue(
        key: Preferences.Key<Boolean>,
        value: Boolean,
    )

    suspend fun getBooleanValue(key: String): Boolean

    fun getBooleanFlow(key: String): Flow<Boolean>

    suspend fun setIntValue(
        key: Preferences.Key<Int>,
        value: Int,
    )

    suspend fun getIntValue(key: String): Int

    suspend fun remove(key: Preferences.Key<String>)

    suspend fun getAuthentication(): Authentication

    suspend fun setAuthentication(authentication: Authentication)

    suspend fun isAuthenticated(): Boolean

    suspend fun saveStatus(
        localStatus: LocalStatus,
        value: Any? = null,
    )

    suspend fun checkStatus(localStatus: LocalStatus): Boolean

    suspend fun clear()
}
