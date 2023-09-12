package com.mfurmanczyk.toodoo.mobile.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.preferences.di.annotation.DataStorePreferences
import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "TooDooAppViewModel"

@HiltViewModel
class TooDooAppViewModel @Inject constructor(
    @DataStorePreferences private val repository: PreferencesRepository
) : ViewModel() {

    private val _shouldDisplayWelcomeScreen: MutableStateFlow<Boolean> = MutableStateFlow(true)
    val shouldDisplayWelcomeScreen = _shouldDisplayWelcomeScreen.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getUsername().collect { username ->

                Log.i(TAG, "username: ${username.toString()}")

                _shouldDisplayWelcomeScreen.update {
                    username == null
                }
            }
        }
    }
}