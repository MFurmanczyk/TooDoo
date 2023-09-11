package com.mfurmanczyk.toodoo.preferences.repository

import kotlinx.coroutines.flow.Flow

interface PreferencesRepository {

    suspend fun getUserName() : Flow<String?>

    suspend fun setUserName(userName: String)

}