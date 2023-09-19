package com.mfurmanczyk.toodoo.mobile.fakedata

import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow


class FakeDataSource {

    private val flow = MutableSharedFlow<String?>()
    suspend fun emit(value: String?) = flow.emit(value)
    fun username(): Flow<String?> = flow

}

class FakePreferencesRepository(val dataSource: FakeDataSource) : PreferencesRepository {

    override fun getUsername(): Flow<String?> = dataSource.username()

    override suspend fun setUsername(username: String) {
        dataSource.emit(username)
    }

    companion object {

        val TEST_STRING: String = "TEST"
        val EMPTY_STRING: String = ""
        val BLANK_STRING: String = " "
    }
}