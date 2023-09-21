package com.mfurmanczyk.toodoo.mobile

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.AddTask
import androidx.compose.material.icons.twotone.Android
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.BookmarkAdd
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.PermanentDrawerSheet
import androidx.compose.material3.PermanentNavigationDrawer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.util.getPagerDestinationsList
import com.mfurmanczyk.toodoo.mobile.view.component.ExpandableFloatingActionButtonState
import com.mfurmanczyk.toodoo.mobile.view.component.TooDooFab
import com.mfurmanczyk.toodoo.mobile.view.component.rememberExpandableFloatingActionButtonState
import com.mfurmanczyk.toodoo.mobile.view.screen.DashboardScreen
import com.mfurmanczyk.toodoo.mobile.view.screen.WelcomeScreen
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
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
    } else {

        var currentDestination by rememberSaveable { mutableIntStateOf(0) }
        val navigationDestinations = getPagerDestinationsList()
        val actionButtonState = rememberExpandableFloatingActionButtonState()
        val pagerState = rememberPagerState { navigationDestinations.size }
        val animationScope = rememberCoroutineScope()

        when (navigationType) {

            NavigationType.BOTTOM_NAV -> {
                BottomNavigationScreen(
                    modifier = modifier,
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
            }

            NavigationType.NAV_RAIL -> {
                NavigationRailScreen(
                    modifier = modifier,
                    username = tooDooAppState.username,
                    currentDestination = currentDestination,
                    navigationDestinations = navigationDestinations,
                    pagerState = pagerState,
                    onNavigationItemClick = {
                        currentDestination = it
                        actionButtonState.collapse()
                        animationScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )
            }

            NavigationType.NAV_DRAWER -> {
                NavigationDrawerScreen(
                    username = tooDooAppState.username,
                    navigationDestinations =navigationDestinations,
                    currentDestination = currentDestination,
                    pagerState = pagerState,
                    modifier = modifier,
                    onNavigationItemClick = {
                        currentDestination = it
                        actionButtonState.collapse()
                        animationScope.launch {
                            pagerState.animateScrollToPage(it)
                        }
                    }
                )
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
private fun BottomNavigationScreen(
    username: String,
    currentDestination: Int,
    navigationDestinations: List<NavigationDestination>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    actionButtonState: ExpandableFloatingActionButtonState = rememberExpandableFloatingActionButtonState(),
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
                        if (currentDestination == 0) stringResource(R.string.hello, username)
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
                        icon = {
                            Icon(
                                item.navigationIcon ?: Icons.TwoTone.Android,
                                contentDescription = stringResource(id = item.displayedTitle)
                            )
                        },
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
            NavigationPager(pagerState, NavigationType.BOTTOM_NAV)
        }
    }
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
private fun NavigationRailScreen(
    username: String,
    currentDestination: Int,
    navigationDestinations: List<NavigationDestination>,
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    onNavigationItemClick: (Int) -> Unit = {}
) {
    Surface(
        modifier = modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.surfaceVariant
    ) {
        Row {
            NavigationRail(
                header = {
                    Spacer(Modifier.padding(top = MaterialTheme.spacing.extraSmall))
                    FloatingActionButton(onClick = { }) {
                        Icon(imageVector = Icons.TwoTone.AddTask, contentDescription = null)
                    }
                    Spacer(Modifier.padding(top = MaterialTheme.spacing.extraSmall))
                    FloatingActionButton(onClick = { }) {
                        Icon(imageVector = Icons.TwoTone.BookmarkAdd, contentDescription = null)
                    }
                }
            ) {
                Spacer(Modifier.weight(1f))
                navigationDestinations.forEachIndexed { index, item ->
                    NavigationRailItem(
                        icon = {
                            Icon(
                                item.navigationIcon ?: Icons.TwoTone.Android,
                                contentDescription = stringResource(id = item.displayedTitle)
                            )
                        },
                        label = { Text(stringResource(id = item.displayedTitle)) },
                        selected = currentDestination == index,
                        onClick = {
                            onNavigationItemClick(index)
                        }
                    )
                }
                Spacer(Modifier.weight(1f))
            }

            Scaffold(
                modifier = modifier,
                topBar = {
                    CenterAlignedTopAppBar(
                        title = {
                            Text(
                                if (currentDestination == 0) stringResource(R.string.hello, username)
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
                }
            ) {
                Surface(
                    modifier = Modifier.padding(it)
                ) {
                    NavigationPager(pagerState, NavigationType.BOTTOM_NAV)
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NavigationDrawerScreen(
    username: String,
    navigationDestinations: List<NavigationDestination>,
    currentDestination: Int,
    pagerState: PagerState,
    modifier: Modifier,
    onNavigationItemClick: (Int) -> Unit = {}
) {
    PermanentNavigationDrawer(
        drawerContent = {
            PermanentDrawerSheet(modifier = Modifier.width(240.dp)) {
                Spacer(Modifier.height(MaterialTheme.spacing.small))
                Text(
                    text = stringResource(R.string.hello, username),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    style = MaterialTheme.typography.headlineSmall
                )
                Spacer(Modifier.height(MaterialTheme.spacing.small))
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.AddTask,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Text(text = stringResource(R.string.new_task))
                    Spacer(modifier = Modifier.weight(1F))
                }
                Spacer(Modifier.padding(top = MaterialTheme.spacing.small))
                ExtendedFloatingActionButton(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = MaterialTheme.spacing.small),
                    onClick = { }
                ) {
                    Icon(
                        imageVector = Icons.TwoTone.BookmarkAdd,
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.weight(1F))
                    Text(text = stringResource(R.string.new_category))
                    Spacer(modifier = Modifier.weight(1F))
                }
                Spacer(Modifier.padding(top = MaterialTheme.spacing.small))
                navigationDestinations.forEachIndexed { index, item ->
                    NavigationDrawerItem(
                        icon = {
                            Icon(
                                imageVector = item.navigationIcon
                                    ?: Icons.TwoTone.Android,
                                contentDescription = stringResource(id = item.displayedTitle)
                            )
                        },
                        label = { Text(stringResource(id = item.displayedTitle)) },
                        selected = currentDestination == index,
                        onClick = {
                            onNavigationItemClick(index)
                        },
                        modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small)
                    )
                }

            }
        }
    ) {
        /////////////
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.surfaceVariant
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            ) {
                Surface(
                    modifier = Modifier
                        .fillMaxSize()
                        .weight(1F),
                    shape = MaterialTheme.shapes.medium
                ) {
                    NavigationPager(
                        pagerState = pagerState,
                        navigationType = NavigationType.NAV_DRAWER
                    )
                }
                Surface(
                    modifier = modifier
                        .fillMaxSize()
                        .weight(1F),
                    color = MaterialTheme.colorScheme.surfaceVariant
                ) {

                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalFoundationApi::class)
private fun NavigationPager(
    pagerState: PagerState,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {
    when (navigationType) {
        NavigationType.BOTTOM_NAV -> {
            HorizontalPager(
                modifier = modifier,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                NavigationPagerContent(page)
            }
        }

        NavigationType.NAV_RAIL -> {
            HorizontalPager(
                modifier = modifier,
                state = pagerState,
                userScrollEnabled = false
            ) { page ->
                NavigationPagerContent(page)
            }
        }

        NavigationType.NAV_DRAWER -> {
            VerticalPager(state = pagerState, userScrollEnabled = false) { page ->
                NavigationPagerContent(page)
            }
        }
    }

}

@Composable
private fun NavigationPagerContent(page: Int) {
    when (page) {
        0 -> {

            val dashboardScreenViewModel: DashboardScreenViewModel = viewModel()
            val dashboardScreenUIState by dashboardScreenViewModel.uiState.collectAsState()

            DashboardScreen(
                uiState = dashboardScreenUIState,
                onTaskCheckedChanged = { task, checked ->
                    dashboardScreenViewModel.checkTask(task, checked)
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