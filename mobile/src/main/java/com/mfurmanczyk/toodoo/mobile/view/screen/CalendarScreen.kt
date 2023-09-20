package com.mfurmanczyk.toodoo.mobile.view.screen

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

class CalendarDestination(context: Context) : NavigationDestination(
    displayedTitle = context.getString(R.string.calendar),
    route = "calendar",
    Icons.TwoTone.CalendarMonth
)