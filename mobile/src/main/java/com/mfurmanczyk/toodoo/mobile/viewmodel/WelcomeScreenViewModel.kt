package com.mfurmanczyk.toodoo.mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidUsernameException
import com.mfurmanczyk.toodoo.preferences.di.annotation.DataStorePreferences
import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "WelcomeScreenViewModel"

data class WelcomeScreenUIState(
    val username: String? = null
)

@HiltViewModel
class WelcomeScreenViewModel @Inject constructor(
    @DataStorePreferences private val preferencesRepository: PreferencesRepository
): ViewModel() {

    private val _uiState : MutableStateFlow<WelcomeScreenUIState> = MutableStateFlow(
        WelcomeScreenUIState()
    )
    val uiState = _uiState.asStateFlow()

    fun updateUsername(username: String) {
        _uiState.update {
            if(username.isBlank()) it.copy(username = null)
            else it.copy(username = username)
        }
    }

    @Throws(InvalidUsernameException::class)
    fun saveUsername() {
        if (_uiState.value.isValid()) {
            Log.i(TAG, "saveUsername: username is valid.")
            viewModelScope.launch {
                _uiState.value.username?.let { preferencesRepository.setUsername(it) }
            }
        }
        else throw InvalidUsernameException()
    }

}

fun WelcomeScreenUIState.isValid() = !this.username.isNullOrBlank()