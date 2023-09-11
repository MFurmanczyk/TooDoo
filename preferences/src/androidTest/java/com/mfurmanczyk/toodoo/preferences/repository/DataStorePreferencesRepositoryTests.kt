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
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(AndroidJUnit4::class)
class DataStorePreferencesRepositoryTests {

    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
    private val testScope: TestScope = TestScope(testDispatcher)
    private val context: Context = ApplicationProvider.getApplicationContext()
    private val dataStorePreferences: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        scope = testScope,
        produceFile = { context.preferencesDataStoreFile("TEST") }
    )

    private val repository = DataStorePreferencesRepository(dataStorePreferences)

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    @Throws(Exception::class)
    fun getUserName_returnsCorrectName() {
        testScope.runTest {

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
        testScope.runTest {
            val actual = repository.getUserName().first()

            assertNull(actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun setUserName_userNameSetCorrectly() {
        testScope.runTest {
            val expected = "Test name"

            repository.setUserName(expected)

            val actual = dataStorePreferences.data.map {
                it[PreferencesProperties.Keys.USERNAME]
            }.first()

            assertEquals(expected, actual)
        }
    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testScope.runTest {
            dataStorePreferences.edit {
                it.clear()
            }
        }
        testScope.cancel()
    }
}