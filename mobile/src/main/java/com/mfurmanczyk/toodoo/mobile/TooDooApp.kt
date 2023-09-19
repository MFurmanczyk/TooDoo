package com.mfurmanczyk.toodoo.mobile

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.DateRange
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mfurmanczyk.toodoo.mobile.util.ContentType
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.component.TooDooFab
import com.mfurmanczyk.toodoo.mobile.view.component.rememberExpandableFloatingActionButtonState
import com.mfurmanczyk.toodoo.mobile.view.screen.DashboardScreen
import com.mfurmanczyk.toodoo.mobile.view.screen.WelcomeScreen
import com.mfurmanczyk.toodoo.mobile.viewmodel.DashboardScreenViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.TooDooAppViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidUsernameException
import kotlinx.coroutines.launch

private const val TAG = "TooDooApp"

@Composable
fun TooDooApp(
    navigationType: NavigationType,
    contentType: ContentType,
    modifier: Modifier = Modifier
) {
    val viewModel: TooDooAppViewModel = viewModel()
    val tooDooAppState by viewModel.state.collectAsState()

    if (tooDooAppState.shouldDisplayWelcomeScreen) {

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
    else {
        SinglePaneScreen(username = tooDooAppState.username)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun SinglePaneScreen(
    username: String,
    modifier: Modifier = Modifier
) {

    val actionButtonState = rememberExpandableFloatingActionButtonState()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navigationDestinations = listOf("Dashboard", "Calendar", "Categories", "Settings")
    val pagerState = rememberPagerState { navigationDestinations.size }
    val animationScope = rememberCoroutineScope()

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedVisibility(
                visible = selectedItem != 3,
                enter = scaleIn(tween(150)),
                exit = scaleOut(tween(150))
            ) {
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
                    Icon(
                        imageVector = Icons.TwoTone.Add,
                        contentDescription = null,
                        modifier = Modifier.rotate(rotation)
                    )
                }
            }
        },
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(if(selectedItem == 0) stringResource(R.string.hello) + username else navigationDestinations[selectedItem] )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = selectedItem != 0,
                        enter = scaleIn(tween(150)),
                        exit = scaleOut(tween(150))
                    ) {
                        IconButton(
                            onClick = {
                                selectedItem = 0
                                animationScope.launch {
                                    pagerState.animateScrollToPage(selectedItem)
                                }
                            }
                        ) {
                            Icon(imageVector = Icons.TwoTone.ArrowBack, contentDescription = null)
                        }

                    }

                }
            )
        },
        bottomBar = {
            NavigationBar {
                navigationDestinations.forEachIndexed { index, item ->
                    NavigationBarItem(
                        icon = { Icon(Icons.Filled.Favorite, contentDescription = item) },
                        label = { Text(item) },
                        selected = selectedItem == index,
                        onClick = {
                            selectedItem = index
                            actionButtonState.collapse()

                            animationScope.launch {
                                pagerState.animateScrollToPage(selectedItem)
                            }
                        }
                    )
                }
            }
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {
            HorizontalPager(state = pagerState, userScrollEnabled = false) { page ->
                when (page) {
                    0 -> {
                        val dashboardScreenViewModel: DashboardScreenViewModel = viewModel()
                        val dashboardScreenUIState by dashboardScreenViewModel.uiState.collectAsState()
                        DashboardScreen(
                            uiState = dashboardScreenUIState,
                            onTaskCheckedChanged = { task, checked ->
                                val updatedTask = task.copy(isDone = checked)
                                dashboardScreenViewModel.updateTask(updatedTask)
                            }
                        )
                    }

                    1 -> {

                    }

                    2 -> {

                    }

                    3 -> {

                    }
                }
            }
        }
    }
}