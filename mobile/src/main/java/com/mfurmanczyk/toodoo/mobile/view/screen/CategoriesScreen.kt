package com.mfurmanczyk.toodoo.mobile.view.screen

import android.content.Context
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Bookmarks
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination

class CategoriesDestination(context: Context) : NavigationDestination(
    displayedTitle = context.getString(R.string.categories),
    route = "categories",
    Icons.TwoTone.Bookmarks
)