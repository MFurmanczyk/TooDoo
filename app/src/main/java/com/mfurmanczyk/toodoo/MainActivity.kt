package com.mfurmanczyk.toodoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import com.mfurmanczyk.toodoo.mobile.TooDooApp
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TooDooTheme {
                val windowWidthSizeClass = calculateWindowSizeClass(this).widthSizeClass
                val windowHeightSizeClass = calculateWindowSizeClass(this).heightSizeClass

                val navigationType = when(windowWidthSizeClass) {
                    WindowWidthSizeClass.Compact -> NavigationType.BOTTOM_NAV
                    WindowWidthSizeClass.Medium -> NavigationType.NAV_RAIL
                    WindowWidthSizeClass.Expanded -> {
                        if(windowHeightSizeClass == WindowHeightSizeClass.Compact) NavigationType.BOTTOM_NAV
                        else NavigationType.NAV_DRAWER
                    }
                    else -> NavigationType.BOTTOM_NAV
                }
                TooDooApp(navigationType = navigationType)
            }
        }
    }
}
