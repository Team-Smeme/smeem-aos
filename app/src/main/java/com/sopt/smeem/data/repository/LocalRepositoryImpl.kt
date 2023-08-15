package com.sopt.smeem.data.repository

import android.content.Context
import android.util.Log
import androidx.datastore.preferences.core.MutablePreferences
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.sopt.smeem.SmeemErrorCode
import com.sopt.smeem.SmeemException
import com.sopt.smeem.data.SmeemDataStore.dataStore
import com.sopt.smeem.domain.model.Authentication
import com.sopt.smeem.domain.repository.LocalRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor(
    private val context: Context
) : LocalRepository {

    /**
     * LocalStorage 로 부터 Authentication 추출
     * 없을 경우, null 응답
     * (already cached on DataStore Layer)
     */
    override suspend fun getAuthentication(): Authentication {
        return context.dataStore.data
            .catch { e: Throwable ->
                Log.e(
                    "dataStore_auth",
                    "로컬스토리지(dataStore) 에 접근해 Authentication 정보를 가져오는 도중 오류가 발생했습니다."
                )
                emit(emptyPreferences())
            }
            .map { preferences: Preferences ->
                Authentication(
                    accessToken = preferences[API_ACCESS_TOKEN] ?: throw SmeemException(
                        SmeemErrorCode.UNAUTHORIZED,
                        "인증이 필요합니다.",
                        IllegalStateException()
                    ),
                    refreshToken = preferences[API_REFRESH_TOKEN] ?: throw SmeemException(
                        SmeemErrorCode.UNAUTHORIZED,
                        "인증이 필요합니다.",
                        IllegalStateException()
                    )
                )
            }.first()
    }

    override suspend fun setAuthentication(authentication: Authentication) {
        try {
            context.dataStore.edit { mutablePreferences: MutablePreferences ->
                mutablePreferences[API_ACCESS_TOKEN] =
                    requireNotNull(authentication.accessToken) { "NPE when register authentication with accessToken" }
                mutablePreferences[API_REFRESH_TOKEN] =
                    requireNotNull(authentication.accessToken) { "NPE when register authentication with refreshToken" }
            }
        } catch (e: IOException) {
            throw SmeemException(errorCode = SmeemErrorCode.SYSTEM_ERROR, throwable = e)
        } catch (e: IllegalArgumentException) {
            throw SmeemException(
                errorCode = SmeemErrorCode.SYSTEM_ERROR,
                logMessage = "token 값 저장 중, null 로 접근하였습니다. (authentication = $authentication)",
                throwable = e
            )
        }
    }

    /**
     * LocalStorage 에 accessToken 이 저장되었는지 확인
     */
    override suspend fun isAuthenticated(): Boolean {
        return context.dataStore.data
            .catch { emit(emptyPreferences()) }
            .map { preferences: Preferences -> preferences[API_ACCESS_TOKEN] }
            .firstOrNull() != null
    }

    override suspend fun clear() {
        try {
            context.dataStore.edit { preferences -> preferences.clear() }
        } catch (t: Throwable) {
            throw SmeemException(errorCode = SmeemErrorCode.SYSTEM_ERROR, throwable = t)
        }
    }

    companion object {
        private val API_ACCESS_TOKEN = stringPreferencesKey("api_access_token")
        private val API_REFRESH_TOKEN = stringPreferencesKey("api_refresh_token")
    }
}