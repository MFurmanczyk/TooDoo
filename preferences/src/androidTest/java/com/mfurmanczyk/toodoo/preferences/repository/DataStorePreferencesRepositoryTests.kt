package com.mfurmanczyk.toodoo.preferences.repository

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mfurmanczyk.toodoo.preferences.keys.PreferencesProperties
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DataStorePreferencesRepositoryTests {

    @get:Rule val folder: TemporaryFolder = TemporaryFolder.builder().assureDeletion().build()

    @OptIn(ExperimentalCoroutinesApi::class)
    private val testDispatcher: TestDispatcher = UnconfinedTestDispatcher()
    private val testScope: TestScope = TestScope(testDispatcher + Job())

    private val dataStorePreferences: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { folder.newFile("test.preferences_pb") }
    )

    private val repository = DataStorePreferencesRepository(dataStorePreferences)

    @Test
    @Throws(Exception::class)
    fun getUserName_returnsCorrectName() {
        runTest {

            val expected = "Test name"

            dataStorePreferences.edit { preferences ->
                preferences[PreferencesProperties.Keys.USERNAME] = expected
            }

            val actual = repository.getUserName().first()

            assertEquals(expected, actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getUserName_returnsNull() {
        runTest {
            val actual = repository.getUserName().first()

            assertNull(actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun setUserName_userNameSetCorrectly() {
        runTest {
            val expected = "Test name"

            repository.setUserName(expected)

            val actual = dataStorePreferences.data.map {
                it[PreferencesProperties.Keys.USERNAME]
            }.first()

            assertEquals(expected, actual)
        }
    }
}