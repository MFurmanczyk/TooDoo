package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun rememberExpandableFloatingActionButtonState(
    isFabExpanded: Boolean = false
) = remember {
    ExpandableFloatingActionButtonState(isFabExpanded)
}

@Stable
class ExpandableFloatingActionButtonState constructor(
    expanded: Boolean = false
) {

    private var isFabExpanded by mutableStateOf(expanded)


    fun expand() {
        isFabExpanded = true
    }

    fun collapse() {
        isFabExpanded = false
    }

    fun isExpanded() = isFabExpanded
}