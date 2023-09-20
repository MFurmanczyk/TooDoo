package com.mfurmanczyk.toodoo.mobile.util

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.mfurmanczyk.toodoo.mobile.view.screen.CalendarDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoriesDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.DashboardDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.SettingsScreen

open class NavigationDestination(
    @StringRes val displayedTitle: Int,
    val route: String,
    val navigationIcon: ImageVector? = null
)

fun getPagerDestinationsList() = listOf(
    DashboardDestination,
    CalendarDestination,
    CategoriesDestination,
    SettingsScreen
)
