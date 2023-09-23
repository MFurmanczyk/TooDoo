package com.mfurmanczyk.toodoo.mobile.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mfurmanczyk.toodoo.mobile.EntryDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoryDisplayDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoryEntryDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.TaskEntryDestination

fun NavGraphBuilder.tooDooNavigationGraph(
    entryContent: @Composable (() -> Unit),
    categoryEntryContent: @Composable (() -> Unit),
    displayCategoryContent: @Composable (() -> Unit),
    taskEntryContent: @Composable (() -> Unit),
) {
    composable(route = EntryDestination.route) {
        entryContent()
    }

    composable(route = CategoryEntryDestination.route) {
        categoryEntryContent()
    }

    composable(
        route = CategoryEntryDestination.parametrizedRoute,
        arguments = listOf(navArgument(CategoryEntryDestination.parameterName) { type = NavType.LongType })
    ) {
        categoryEntryContent()
    }

    composable(
        route = CategoryDisplayDestination.parametrizedRoute,
        arguments = listOf(navArgument(CategoryDisplayDestination.parameterName) { type = NavType.LongType })
    ) {
        displayCategoryContent()
    }

    composable(route = TaskEntryDestination.route) {
        taskEntryContent()
    }

    composable(
        route = TaskEntryDestination.parametrizedRoute,
        arguments = listOf(navArgument(TaskEntryDestination.parameterName) { type = NavType.LongType })
    ) {
        taskEntryContent()
    }
}