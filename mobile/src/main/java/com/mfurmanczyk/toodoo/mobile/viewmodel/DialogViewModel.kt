package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

data class DialogDisplayState(
    val shouldDisplayDialog: Boolean
)

open class DialogViewModel : ViewModel() {

    private val _dialogState = MutableStateFlow(DialogDisplayState(false))
    val dialogState = _dialogState.asStateFlow()


    fun displayDialog() {
        _dialogState.update {
            it.copy(shouldDisplayDialog = true)
        }
    }

    fun hideDialog() {
        _dialogState.update {
            it.copy(shouldDisplayDialog = false)
        }
    }
}