package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

object CalendarDestination : NavigationDestination(
    displayedTitle = R.string.calendar,
    route = "calendar",
    Icons.TwoTone.CalendarMonth
)