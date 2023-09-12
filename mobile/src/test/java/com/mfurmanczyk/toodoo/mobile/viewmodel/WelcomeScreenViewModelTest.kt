package com.mfurmanczyk.toodoo.mobile.viewmodel

import com.mfurmanczyk.toodoo.mobile.exception.InvalidUsernameException
import com.mfurmanczyk.toodoo.mobile.fakedata.FakePreferencesRepository
import com.mfurmanczyk.toodoo.mobile.rule.MainDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class WelcomeScreenViewModelTest {

    @ExperimentalCoroutinesApi
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(UnconfinedTestDispatcher())

    private lateinit var repository: FakePreferencesRepository
    private lateinit var viewModel: WelcomeScreenViewModel

    @Before
    fun setup() {
        repository =  FakePreferencesRepository()
        viewModel = WelcomeScreenViewModel(repository)
    }

    @Test
    @Throws(Exception::class)
    fun updateUsername_filledUsername_uiStateUpdatedWithUsername() {
        runTest {

            viewModel.updateUsername(FakePreferencesRepository.TEST_STRING)

            val actual = viewModel.uiState.value.username

            assertEquals(FakePreferencesRepository.TEST_STRING, actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateUsername_blankUsername_uiStateUpdatedWithNull() {
        runTest {

            viewModel.updateUsername(FakePreferencesRepository.BLANK_STRING)
            val actual = viewModel.uiState.value.username

            assertNull(actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveUsername_savingFilledUsername_usernameSavedCorrectly() {
        runTest {
            viewModel.updateUsername(FakePreferencesRepository.TEST_STRING)
            viewModel.saveUsername()

            val actual = repository.getUsername().first()

            assertEquals(FakePreferencesRepository.TEST_STRING, actual)
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveUsername_savingNullUsername_throwsInvalidUsernameException() {
        runTest {
            assertThrows(InvalidUsernameException::class.java) {
                viewModel.saveUsername()
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveUsername_savingSpaceAsUsername_throwsInvalidUsernameException() {
        runTest {
            assertThrows(InvalidUsernameException::class.java) {
                viewModel.updateUsername(FakePreferencesRepository.BLANK_STRING)
                viewModel.saveUsername()
            }
        }
    }

    @Test
    @Throws(Exception::class)
    fun saveUsername_savingEmptyStringAsUsername_throwsInvalidUserNameException() {
        runTest {
            assertThrows(InvalidUsernameException::class.java) {
                viewModel.updateUsername(FakePreferencesRepository.EMPTY_STRING)
                viewModel.saveUsername()
            }
        }
    }
}