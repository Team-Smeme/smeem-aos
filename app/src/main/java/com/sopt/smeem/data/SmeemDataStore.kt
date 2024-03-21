package com.sopt.smeem.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

object SmeemDataStore {
    val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "storage")

    val RECENT_DIARY_DATE = stringPreferencesKey("recent_diary_date")
}