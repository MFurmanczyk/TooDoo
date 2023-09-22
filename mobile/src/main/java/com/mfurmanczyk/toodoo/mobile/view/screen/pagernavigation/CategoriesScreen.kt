package com.mfurmanczyk.toodoo.mobile.view.screen.pagernavigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Bookmarks
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

object CategoriesDestination : NavigationDestination(
    displayedTitle = R.string.categories,
    route = "categories",
    Icons.TwoTone.Bookmarks
)