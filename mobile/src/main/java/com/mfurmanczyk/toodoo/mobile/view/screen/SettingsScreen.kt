package com.mfurmanczyk.toodoo.mobile.view.screen

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

class SettingsScreen(context: Context) : NavigationDestination(
    displayedTitle = context.getString(R.string.settings),
    route = "settings",
    navigationIcon = Icons.TwoTone.Settings
)
