package com.mfurmanczyk.toodoo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mfurmanczyk.toodoo.mobile.util.ContentType
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.TooDooApp
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TooDooTheme {
                TooDooApp(navigationType = NavigationType.BOTTOM_NAV, contentType = ContentType.SINGLE_PANE)
            }
        }
    }
}
