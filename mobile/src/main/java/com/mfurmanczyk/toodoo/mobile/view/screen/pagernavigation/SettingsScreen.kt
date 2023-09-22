package com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

object SettingsScreen: NavigationDestination(
    displayedTitle = R.string.settings,
    route = "settings",
    navigationIcon = Icons.TwoTone.Settings
)

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier
) {

}
