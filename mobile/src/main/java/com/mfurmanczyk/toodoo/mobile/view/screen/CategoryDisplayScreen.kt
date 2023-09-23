package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mfurmanczyk.toodoo.mobile.EntryDestination
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.component.ConfirmationDialog
import com.mfurmanczyk.toodoo.mobile.view.component.TaskTile
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.CategoryDisplayViewModel

object CategoryDisplayDestination : NavigationDestination(
    displayedTitle = R.string.category,
    route = "category"
) {
    const val parameterName = "categoryId"

    val parametrizedRoute = "$route/{$parameterName}"
    fun destinationWithParam(categoryId: Long) = "$route/$categoryId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryDisplayScreen(
    navController: NavHostController,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<CategoryDisplayViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(uiState.category.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                actions = {
                    //Edit
                    IconButton(
                        onClick = {
                            navController.navigate(
                                CategoryEntryDestination.destinationWithParam(
                                    viewModel.categoryId
                                )
                            )
                        }
                    ) {
                        Icon(imageVector = Icons.TwoTone.Edit, contentDescription = null)
                    }
                    //Delete
                    IconButton(onClick = viewModel::displayDialog) {
                        Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (navigationType != NavigationType.NAV_DRAWER) navController.navigateUp()
                            else navController.navigate(EntryDestination.route)
                        }
                    ) {
                        Icon(
                            imageVector = if (navigationType != NavigationType.NAV_DRAWER) Icons.TwoTone.ArrowBack else Icons.TwoTone.Close,
                            contentDescription = null
                        )
                    }
                })
        }
    ) {
        Surface(
            modifier = Modifier.padding(it)
        ) {

            LazyColumn(contentPadding = PaddingValues(horizontal = MaterialTheme.spacing.small)) {
                items(uiState.taskList) {
                    TaskTile(
                        onClick = { },
                        onCheckboxClick = viewModel::checkTask,
                        task = it
                    )
                }
            }

            if(dialogState.shouldDisplayDialog) {
                ConfirmationDialog(
                    onDismissRequest = viewModel::hideDialog,
                    onConfirmation = {
                        viewModel.deleteCategory()
                        viewModel.hideDialog()
                        navController.navigate(EntryDestination.route)
                    },
                    dialogTitle = stringResource(R.string.confirm_deletion),
                    dialogText = stringResource(R.string.confirm_deletion_message),
                    icon = Icons.TwoTone.Warning
                )
            }
        }
    }
}


