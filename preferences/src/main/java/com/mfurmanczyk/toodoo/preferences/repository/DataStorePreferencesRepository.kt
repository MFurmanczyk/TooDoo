package com.mfurmanczyk.toodoo.preferences.repository

import android.util.Log
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.mfurmanczyk.toodoo.preferences.keys.PreferencesProperties
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException
import javax.inject.Inject

private const val TAG = "DataStorePreferencesRep"

class DataStorePreferencesRepository @Inject constructor(
    private val preferencesDataStore: DataStore<Preferences>
) : PreferencesRepository {

    override suspend fun getUsername(): Flow<String?> = preferencesDataStore.data
        .catch {exception ->
            if(exception is IOException) {
                Log.e(TAG, "Error reading preferences", exception)
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }.map { preferences ->
        preferences[PreferencesProperties.Keys.USERNAME]
    }

    override suspend fun setUsername(username: String) {
        preferencesDataStore.edit { preferences ->
            preferences[PreferencesProperties.Keys.USERNAME] = username
        }
    }
}