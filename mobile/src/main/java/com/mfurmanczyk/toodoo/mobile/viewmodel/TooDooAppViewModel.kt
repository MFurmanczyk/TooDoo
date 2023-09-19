package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.preferences.di.annotation.DataStorePreferences
import com.mfurmanczyk.toodoo.preferences.repository.PreferencesRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

private const val TAG = "TooDooAppViewModel"

data class TooDooAppState(
    val username: String,
    val shouldDisplayWelcomeScreen: Boolean
)

@HiltViewModel
class TooDooAppViewModel @Inject constructor(
    @DataStorePreferences private val repository: PreferencesRepository
) : ViewModel() {

    val state =  repository.getUsername().map {
        if(it.isNullOrBlank()) {
            TooDooAppState("User", true)
        } else {
            TooDooAppState(it, false)
        }
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        TooDooAppState("User", false)
    )

    companion object {
        val TIMEOUT_MILLIS = 5000L
    }
}