package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material.icons.twotone.Edit
import androidx.compose.material.icons.twotone.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.mobile.EntryDestination
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.view.component.ConfirmationDialog
import com.mfurmanczyk.toodoo.mobile.view.component.StepTile
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.TaskDisplayUiState
import com.mfurmanczyk.toodoo.mobile.viewmodel.TaskDisplayViewModel
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TaskDisplayDestination : NavigationDestination(
    displayedTitle = R.string.task,
    route = "task"
) {
    override val route: String = super.route
    const val parameterName = "stepId"

    val parametrizedRoute = "$route/{$parameterName}"
    fun destinationWithParam(taskId: Long) = "$route/$taskId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskDisplayScreen(
    navController: NavHostController,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<TaskDisplayViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val dialogState by viewModel.dialogState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(uiState.task.name, maxLines = 1, overflow = TextOverflow.Ellipsis) },
                actions = {
                    //Edit
                    if(uiState.task.id != 0L) {
                        IconButton(
                            onClick = {
                                navController.navigate(
                                    TaskEntryDestination.destinationWithParam(
                                        uiState.task.id
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
        TaskDisplayScreenContent(
            uiState = uiState,
            onStepChecked = viewModel::checkStep,
            onTaskChecked = viewModel::checkTask,
            modifier = Modifier.padding(it),
        )

        if(dialogState.shouldDisplayDialog) {
            ConfirmationDialog(
                onDismissRequest = viewModel::hideDialog,
                onConfirmation = {
                    viewModel.deleteTask()
                    viewModel.hideDialog()
                    navController.navigate(EntryDestination.route)
                },
                dialogTitle = stringResource(id = R.string.confirm_deletion_title),
                dialogText = stringResource(R.string.confirm_deletion_task),
                icon = Icons.TwoTone.Warning
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TaskDisplayScreenContent(
    uiState: TaskDisplayUiState,
    onStepChecked: (Step, Boolean) -> Unit,
    onTaskChecked: (Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ){
            var descriptionExpanded by rememberSaveable {
                mutableStateOf(false)
            }
            Text(
                text = "${stringResource(id = R.string.task_description)}:"
            )
            AnimatedContent(targetState = descriptionExpanded, label = "Task description animation") {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        descriptionExpanded = !descriptionExpanded
                    },
                    shape = MaterialTheme.shapes.medium
                ) {
                    Text(
                        modifier = Modifier.padding(MaterialTheme.spacing.medium),
                        text = uiState.task.description ?: "",
                        maxLines = if(it) Int.MAX_VALUE else 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }

            TaskFieldDisplay(
                fieldName = stringResource(id = R.string.due_date),
                fieldValue = uiState.task.dueDate.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
            )

            TaskFieldDisplay(
                fieldName = stringResource(R.string.created_on),
                fieldValue = uiState.task.createdOn.toLocalDate().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy"))
            )

            TaskFieldDisplay(
                fieldName = stringResource(R.string.completed_on),
                fieldValue =uiState.task.completedOn?.toLocalDate()?.format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")) ?: ""
            )

            TaskFieldDisplay(
                fieldName = stringResource(id = R.string.category),
                fieldValue = uiState.category.name
            )

            TextButton(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    onTaskChecked(!uiState.task.isDone)
                },
                contentPadding = PaddingValues(MaterialTheme.spacing.small),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(
                    imageVector = if (uiState.task.isDone) Icons.TwoTone.Close else Icons.TwoTone.Check,
                    contentDescription = null
                )
                Text(
                    text = (if(uiState.task.isDone) "Mark undone" else "Mark done").uppercase()
                )
            }

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                items(uiState.steps) {step ->
                    StepTile(
                        step = step,
                        onStepChecked = onStepChecked,
                        onRemoveClick = {/*nothing to do here*/},
                        editMode = false
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskDisplayScreenPreview() {
    TooDooTheme {
        TaskDisplayScreenContent(
            uiState = TaskDisplayUiState(
                task = Task(
                    name = "Task not found",
                    description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Integer ac ante at magna congue dapibus ac lobortis augue. Vestibulum eu euismod leo, eu imperdiet dolor. Proin interdum eu arcu non rhoncus. Ut vitae vehicula sapien. Sed vulputate lorem nec enim condimentum, id commodo dui posuere. Sed in laoreet lorem. Sed mi leo, tristique tincidunt tortor ac, sollicitudin auctor lacus. Aenean aliquet vel ex in dapibus.",
                    createdOn = LocalDateTime.now(),
                    dueDate = LocalDate.now(),
                    isDone = false
                ),
                category = Category.uncategorizedCategory(),
                steps = listOf()
            ), onStepChecked = {_, _ ->}, onTaskChecked = {}
        )
    }
}

@Composable
private fun TaskFieldDisplay(
    fieldName: String,
    fieldValue: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$fieldName:")
        Spacer(modifier = Modifier.weight(1F))
        Text(text = fieldValue)
    }
}

@Preview
@Composable
fun TaskFieldDisplayPreview() {
    TooDooTheme {
        TaskFieldDisplay(fieldName = "Due date", fieldValue = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, dd MMMM yyyy")))
    }
}