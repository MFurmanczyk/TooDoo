package com.mfurmanczyk.toodoo.mobile.view.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.mfurmanczyk.toodoo.mobile.EntryDestination
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoryEntryDestination

fun NavGraphBuilder.tooDooNavigationGraph(
    entryContent: @Composable (() -> Unit),
    newCategoryContent: @Composable (() -> Unit),
    editCategoryContent: @Composable (() -> Unit),
) {
    composable(route = EntryDestination.route) {
        entryContent()
    }

    composable(route = CategoryEntryDestination.route) {
        newCategoryContent()
    }

    composable(
        route = CategoryEntryDestination.parametrizedRoute,
        arguments = listOf(navArgument(CategoryEntryDestination.parameterName) { type = NavType.LongType })
    ) {
        editCategoryContent()
    }
}