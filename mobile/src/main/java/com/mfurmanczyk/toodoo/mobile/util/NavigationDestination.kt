package com.mfurmanczyk.toodoo.mobile.util

import android.content.Context
import androidx.compose.ui.graphics.vector.ImageVector
import com.mfurmanczyk.toodoo.mobile.view.screen.CalendarDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoriesDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.DashboardDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.SettingsScreen

open class NavigationDestination(
    val displayedTitle: String,
    val route: String,
    val navigationIcon: ImageVector? = null
)

fun getDestinationLists(context: Context) = listOf(
    DashboardDestination(context),
    CalendarDestination(context),
    CategoriesDestination(context),
    SettingsScreen(context)

)
