package com.mfurmanczyk.toodoo.mobile.util

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.CalendarDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.CategoriesDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.DashboardDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation.SettingsScreen

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
