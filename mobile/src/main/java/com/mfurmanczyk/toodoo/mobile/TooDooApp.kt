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
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddTask
import androidx.compose.material.icons.twotone.Android
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.BookmarkAdd
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.util.getPagerDestinationsList
import com.mfurmanczyk.toodoo.mobile.view.component.ExpandableFloatingActionButtonState
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

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun TooDooApp(
    navigationType: NavigationType,
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

        var currentDestination by rememberSaveable { mutableIntStateOf(0) }
        val navigationDestinations = getPagerDestinationsList()
        val actionButtonState = rememberExpandableFloatingActionButtonState()
        val pagerState = rememberPagerState { navigationDestinations.size }
        val animationScope = rememberCoroutineScope()

        when(navigationType) {

            NavigationType.BOTTOM_NAV -> BottomNavigationScreen(
                username = tooDooAppState.username,
                currentDestination = currentDestination,
                navigationDestinations = navigationDestinations,
                actionButtonState = actionButtonState,
                pagerState = pagerState,
                onNavigationItemClick = {
                    currentDestination = it
                    actionButtonState.collapse()
                    animationScope.launch {
                        pagerState.animateScrollToPage(it)
                    }
                }
            )

            NavigationType.NAV_RAIL -> TODO()

            NavigationType.NAV_DRAWER -> TODO()
        }

    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun BottomNavigationScreen(
    username: String,
    currentDestination: Int,
    navigationDestinations: List<NavigationDestination>,
    modifier: Modifier = Modifier,
    actionButtonState: ExpandableFloatingActionButtonState = rememberExpandableFloatingActionButtonState(),
    pagerState: PagerState = rememberPagerState(initialPage = 1, initialPageOffsetFraction = 0f) { 4 },
    onNavigationItemClick: (Int) -> Unit = {}
) {

    Scaffold(
        modifier = modifier,
        floatingActionButton = {
            AnimatedVisibility(
                visible = currentDestination != 3,
                enter = scaleIn(tween(150)),
                exit = scaleOut(tween(150))
            ) {
                TooDooFab(
                    state = actionButtonState,
                    onFirstActionClick = { /*TODO*/ },
                    onSecondActionClick = { /*TODO*/ },
                    firstActionContent = {
                        Icon(imageVector = Icons.TwoTone.BookmarkAdd, contentDescription = null)
                    },
                    secondActionContent = {
                        Icon(imageVector = Icons.TwoTone.AddTask, contentDescription = null)
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
                    Text(
                        if(currentDestination == 0) stringResource(R.string.hello, username)
                        else stringResource(id = navigationDestinations[currentDestination].displayedTitle)
                    )
                },
                navigationIcon = {
                    AnimatedVisibility(
                        visible = currentDestination != 0,
                        enter = scaleIn(tween(150)),
                        exit = scaleOut(tween(150))
                    ) {
                        IconButton(
                            onClick = {
                                onNavigationItemClick(0)
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
                        icon = { Icon(item.navigationIcon ?: Icons.TwoTone.Android, contentDescription = stringResource(id = item.displayedTitle)) },
                        label = { Text(stringResource(id = item.displayedTitle)) },
                        selected = currentDestination == index,
                        onClick = {
                            onNavigationItemClick(index)
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
                        //CalendarScreenViewModel
                        //CalendarScreenUIState
                    }

                    2 -> {
                        //CategoriesScreenViewModel
                        //CategoriesScreenUIState
                    }
                    3 -> {
                        //SettingsScreenViewModel
                        //SettingsScreenUIState
                    }
                }
            }
        }
    }
}