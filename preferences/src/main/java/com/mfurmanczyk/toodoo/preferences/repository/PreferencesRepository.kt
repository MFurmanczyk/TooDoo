package com.mfurmanczyk.toodoo.preferences.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun getUsername() : Flow<String?>

    suspend fun setUsername(username: String)

}