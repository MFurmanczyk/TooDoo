package com.mfurmanczyk.toodoo.mobile

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mfurmanczyk.toodoo.mobile.exception.InvalidUsernameException
import com.mfurmanczyk.toodoo.mobile.screen.WelcomeScreen
import com.mfurmanczyk.toodoo.mobile.util.ContentType
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.viewmodel.TooDooAppViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenViewModel

private const val TAG = "TooDooApp"

@Composable
fun TooDooApp(
    navigationType: NavigationType,
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    val viewModel: TooDooAppViewModel = viewModel()
    val shouldDisplayWelcomeScreen by viewModel.shouldDisplayWelcomeScreen.collectAsState()

    if(shouldDisplayWelcomeScreen) {

        val welcomeScreenViewModel: WelcomeScreenViewModel = viewModel()
        val welcomeScreenUIState by welcomeScreenViewModel.uiState.collectAsState()

        val context = LocalContext.current
        val focusManager = LocalFocusManager.current

        WelcomeScreen(
            uiState = welcomeScreenUIState,
            onInputFieldValueChanged = welcomeScreenViewModel::updateUsername,
            onSaveClick = {
                try {
                    welcomeScreenViewModel.saveUsername()
                    focusManager.clearFocus()
                } catch (e: InvalidUsernameException) {
                    Log.w(TAG, "TooDooApp: ", e)
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = modifier
        )
    }
}