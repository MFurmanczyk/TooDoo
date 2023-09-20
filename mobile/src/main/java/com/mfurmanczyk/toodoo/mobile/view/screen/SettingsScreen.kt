package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

object SettingsScreen: NavigationDestination(
    displayedTitle = R.string.settings,
    route = "settings",
    navigationIcon = Icons.TwoTone.Settings
)
