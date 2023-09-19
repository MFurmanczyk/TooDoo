package com.mfurmanczyk.toodoo.mobile.fakedata

import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FakePreferencesRepository() : PreferencesRepository {

    private var username: String? = INITIAL_STRING

    override fun getUsername(): Flow<String?> = flow {
        emit(username)
    }

    override suspend fun setUsername(username: String) {
        this.username = username
    }

    companion object {

        val INITIAL_STRING: String = "INIT"
        val TEST_STRING: String = "TEST"
        val EMPTY_STRING: String = ""
        val BLANK_STRING: String = " "
    }
}