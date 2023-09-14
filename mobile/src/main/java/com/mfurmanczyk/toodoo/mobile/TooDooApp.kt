package com.mfurmanczyk.toodoo.mobile

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mfurmanczyk.toodoo.mobile.util.ContentType
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.component.TooDooFab
import com.mfurmanczyk.toodoo.mobile.view.component.rememberExpandableFloatingActionButtonState
import com.mfurmanczyk.toodoo.mobile.view.screen.WelcomeScreen
import com.mfurmanczyk.toodoo.mobile.viewmodel.TooDooAppViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidUsernameException

private const val TAG = "TooDooApp"

@OptIn(ExperimentalMaterial3Api::class)
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
    } else {

        val actionButtonState = rememberExpandableFloatingActionButtonState()

        var selectedItem by remember { mutableIntStateOf(0) }
        val items = listOf("Dashboard", "Calendar", "Categories", "Settings")

        Scaffold(
            floatingActionButton = {
                AnimatedVisibility(visible = selectedItem != 3, enter = scaleIn(tween(150)), exit = scaleOut(tween(150))) {
                    TooDooFab(
                        state = actionButtonState,
                        onFirstActionClick = { /*TODO*/ },
                        onSecondActionClick = { /*TODO*/ },
                        firstActionContent = {
                            Icon(imageVector = Icons.TwoTone.DateRange, contentDescription = null)
                        },
                        secondActionContent = {
                            Icon(imageVector = Icons.TwoTone.AddCircle, contentDescription = null)
                        }
                        ) {
                        val rotation by animateFloatAsState(targetValue = if (actionButtonState.isExpanded()) 45f else 0f)
                        Icon(imageVector = Icons.TwoTone.Add, contentDescription = null, modifier = Modifier.rotate(rotation))
                    }
                }
            },
            topBar = {
                     TopAppBar(title = { Text(text = "Hello, user!") }) //TODO: customisable text, back button, disappearing icons
            },
            bottomBar = {
                AnimatedVisibility(
                    visible = selectedItem != 3,
                    enter = slideInVertically(
                        animationSpec = tween(150)
                    ) {
                        2 * it
                      },
                    exit = slideOutVertically(
                        animationSpec = tween(150)
                    ) {
                        2 * it
                    }
                ) {
                    NavigationBar {

                        NavigationBar {
                            items.forEachIndexed { index, item ->
                                NavigationBarItem(
                                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                                    label = { Text(item) },
                                    selected = selectedItem == index,
                                    onClick = {
                                        selectedItem = index
                                        actionButtonState.collapse()
                                    }
                                )
                            }
                        }
                    }
                }
            }
        ) {
            Surface(
                modifier = Modifier.padding(it)
            ) {

            }
        }

    }
}