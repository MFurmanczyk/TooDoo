package com.mfurmanczyk.toodoo.mobile.view.screen

import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.mobile.EntryDestination
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.view.component.InputField
import com.mfurmanczyk.toodoo.mobile.view.component.StepTile
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.TaskEntryUiState
import com.mfurmanczyk.toodoo.mobile.viewmodel.TaskEntryViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidStepDescriptionException
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidTaskNameException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TaskEntryDestination : NavigationDestination(
    displayedTitle = R.string.new_task,
    route = "new_task"
) {
    const val parameterName = "taskId"
    private const val editTaskRoute = "edit_task"

    val parametrizedRoute = "$editTaskRoute/{$parameterName}"
    fun destinationWithParam(taskId: Long) = "$editTaskRoute/$taskId"
}

private const val TAG = "TaskEntryScreen"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TaskEntryScreen(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    val viewModel = hiltViewModel<TaskEntryViewModel>()
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(text = if(uiState.newEntry) stringResource(id = R.string.new_task) else uiState.taskName)
                },
                actions = {
                    val context = LocalContext.current
                    IconButton(
                        onClick = {
                            try {
                                viewModel.addNewTaskWithSteps()
                                if(uiState.newEntry) navController.navigate(EntryDestination.route)
                                else navController.navigateUp()
                            } catch (e: InvalidTaskNameException) {
                                Toast.makeText(context, context.getText(e.displayMessage), Toast.LENGTH_SHORT).show()
                            }
                        }) {
                        Icon(imageVector = Icons.TwoTone.Check, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (uiState.newEntry) navController.navigate(
                            EntryDestination.route
                        ) else navController.navigateUp()
                    }) {
                        Icon(
                            imageVector =
                                if(uiState.newEntry)
                                    Icons.TwoTone.Close
                                else
                                    Icons.TwoTone.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        val context = LocalContext.current

        TaskEntryScreenContent(
            uiState = uiState,
            onTaskNameValueChanged = viewModel::updateTaskName,
            onTaskDescriptionValueChanged = viewModel::updateTaskDescription,
            onCategorySelected = viewModel::updateSelectedCategory,
            onDateValueChanged = { /*TODO*/ },
            onDatePickerClick = { /*TODO*/ },
            modifier = Modifier.padding(it),
            onStepChecked = { _, _ -> /* NOTHING TO DO */},
            onStepRemoveClick = viewModel::deleteStep,
            onStepDescriptionValueChanged = viewModel::updateStepDescription,
            onStepConfirmClick = {
                try {
                    viewModel.addStep(it)
                    viewModel.switchStepEntryMode(false)
                } catch (e: InvalidStepDescriptionException) {
                    Log.w(TAG, "TaskEntryScreen: ${e.message}")
                    Toast.makeText(context, context.getText(R.string.step_description_warning), Toast.LENGTH_SHORT).show()
                }

            },
            onStepCancelClick = { viewModel.switchStepEntryMode(false) },
            onAddStepClick = { viewModel.switchStepEntryMode(true) },
        )
    }

}

@Composable
fun TaskEntryScreenContent(
    uiState: TaskEntryUiState,
    onTaskNameValueChanged: (String) -> Unit,
    onTaskDescriptionValueChanged: (String) -> Unit,
    onCategorySelected: (Category) -> Unit,
    onDateValueChanged: (String) -> Unit,
    onDatePickerClick: () -> Unit,
    modifier: Modifier = Modifier,
    onStepChecked: (Step, Boolean) -> Unit,
    onStepRemoveClick: (Step) -> Unit,
    onStepDescriptionValueChanged: (String) -> Unit,
    onStepConfirmClick: (String) -> Unit,
    onStepCancelClick: () -> Unit,
    onAddStepClick: () -> Unit,

    ) {
        Surface(modifier = modifier.fillMaxSize()) {
            Column(
                modifier = Modifier.padding(horizontal = MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
            ) {
                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.task_name),
                    value = uiState.taskName,
                    onValueChanged = onTaskNameValueChanged,
                    maxLines = 1,
                    singleLine = true
                )
                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.task_description),
                    value = uiState.taskDescription ?: "",
                    onValueChanged = onTaskDescriptionValueChanged,
                    maxLines = 5,
                    singleLine = false
                )

                var menuExpanded by remember {
                    mutableStateOf(false)
                }

                CategoryPickerDropdownMenu(
                    modifier = Modifier.fillMaxWidth(),
                    expanded = menuExpanded,
                    label = stringResource(id = R.string.category),
                    uiState = uiState,
                    onExpandedChanged = {
                        menuExpanded = !menuExpanded
                    },
                    onCategorySelected = {
                        onCategorySelected(it)
                        menuExpanded = !menuExpanded
                    }
                )

                InputField(
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.due_date),
                    value = uiState.dueDate.format(DateTimeFormatter.ofPattern("dd MMM yyyy")),
                    onValueChanged = onDateValueChanged,
                    trailingIcon = {
                        IconButton(onClick = onDatePickerClick) {
                            Icon(imageVector = Icons.TwoTone.CalendarMonth, contentDescription = null)
                        }
                    },
                    readOnly = true
                )
                Text(text = stringResource(R.string.steps), style = MaterialTheme.typography.labelMedium)
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
                ) {
                    items(uiState.stepsList) {
                        StepTile(
                            step = it,
                            onStepChecked = onStepChecked,
                            onRemoveClick = onStepRemoveClick,
                            editMode = true
                        )
                    }
                    item {
                        AnimatedContent(targetState = uiState.isAddingNewStep, label = "Animated input tile") {
                            if(it) {
                                StepEntryTile(
                                    stepDescription = uiState.stepDescription ?: "",
                                    onDescriptionValueChanged = onStepDescriptionValueChanged,
                                    onConfirmClick = onStepConfirmClick,
                                    onCancelClick = onStepCancelClick
                                )
                            } else {
                                NewStepTile(
                                    text = stringResource(R.string.add_step).uppercase(),
                                    icon = Icons.TwoTone.Add,
                                    onClick = onAddStepClick
                                )
                            }
                        }
                    }
                }
            }
        }
    }


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryPickerDropdownMenu(
    uiState: TaskEntryUiState,
    label: String,
    modifier: Modifier = Modifier,
    onExpandedChanged: (Boolean) -> Unit,
    onCategorySelected: (Category) -> Unit,
    expanded: Boolean = false
) {
    ExposedDropdownMenuBox(
        modifier = modifier,
        expanded = expanded,
        onExpandedChange = onExpandedChanged
    ) {
        InputField(
            modifier = Modifier
                .menuAnchor()
                .fillMaxWidth(),
            label = label,
            value = uiState.selectedCategoryName ?: "",
            onValueChanged = { },
            maxLines = 1,
            readOnly = true,
            singleLine = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) }
        )
        ExposedDropdownMenu(
            modifier = Modifier.fillMaxWidth(),
            expanded = expanded,
            onDismissRequest = {
                onExpandedChanged(false)
            }
        ) {
            if(uiState.categoryList.isEmpty()) {
                DropdownMenuItem(
                    text = {
                        Text(text = stringResource(R.string.no_categories))
                    },
                    onClick = { onExpandedChanged(false) }
                )
            } else {
                uiState.categoryList.forEach {
                    DropdownMenuItem(
                        text = { Text(it.name) },
                        onClick = {
                            onCategorySelected(it)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun StepEntryTile(
    stepDescription: String,
    onDescriptionValueChanged: (String) -> Unit,
    onConfirmClick: (String) -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(text = stringResource(R.string.step_description))
                },
                shape = MaterialTheme.shapes.medium,
                value = stepDescription,
                onValueChange = onDescriptionValueChanged,
                maxLines = 1,
                trailingIcon = {
                    Row {
                        IconButton(onClick = { onConfirmClick(stepDescription) }){
                            Icon(imageVector = Icons.TwoTone.Check, contentDescription = null)
                        }
                        IconButton(onClick = onCancelClick) {
                            Icon(imageVector = Icons.TwoTone.Close, contentDescription = null)
                        }
                    }

                }
            )
        }
    }
}

@Composable
fun NewStepTile(
    text: String,
    icon: ImageVector,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier.fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ){
        TextButton(
            onClick = onClick,
            contentPadding = PaddingValues(MaterialTheme.spacing.medium),
            shape = MaterialTheme.shapes.medium
        ) {
            Icon(imageVector = icon, contentDescription = null)
            Text(text = text)
        }
    }

}

@Preview
@Composable
fun TaskEntryScreenContentPreview() {
    TooDooTheme {
        TaskEntryScreenContent(
            uiState = TaskEntryUiState(newEntry = true, createdOn = LocalDateTime.now()),
            onTaskNameValueChanged = {

            },
            onTaskDescriptionValueChanged = {

            },
            onCategorySelected = {

            },
            onDateValueChanged = {

            },
            onDatePickerClick = {

            },
            onStepChecked = { _, _ ->

            },
            onStepRemoveClick = {

            },
            onStepDescriptionValueChanged = {

            },
            onStepConfirmClick = {

            },
            onStepCancelClick = {

            },
            onAddStepClick = {

            }
        )
    }
}

@Preview
@Composable
fun TaskEntryScreenContentPreview_newStep() {
    TooDooTheme {
        TaskEntryScreenContent(
            uiState = TaskEntryUiState(newEntry = true, isAddingNewStep = true, createdOn = LocalDateTime.now()),
            onTaskNameValueChanged = {

            },
            onTaskDescriptionValueChanged = {

            },
            onCategorySelected = {

            },
            onDateValueChanged = {

            },
            onDatePickerClick = {

            },
            onStepChecked = { _, _ ->

            },
            onStepRemoveClick = {

            },
            onStepDescriptionValueChanged = {

            },
            onStepConfirmClick = {

            },
            onStepCancelClick = {

            },
            onAddStepClick = {

            }
        )
    }
}

@Preview
@Composable
fun TilesPreview() {
    TooDooTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small),
                modifier = Modifier.padding(MaterialTheme.spacing.small)
            ) {
                StepTile(
                    step = Step(
                        description = "To do something.",
                        createdOn = LocalDateTime.now(),
                        isDone = false
                    ),
                    onRemoveClick = {},
                    onStepChecked = {_, _ ->},
                    editMode = true
                )
                StepTile(
                    step = Step(
                        description = "To do something.",
                        createdOn = LocalDateTime.now(),
                        isDone = true
                    ),
                    onRemoveClick = {},
                    onStepChecked = {_, _ ->},
                    editMode = true
                )
                StepTile(
                    step = Step(
                        description = "To do something.",
                        createdOn = LocalDateTime.now(),
                        isDone = false
                    ),
                    onRemoveClick = {},
                    onStepChecked = {_, _ ->},
                    editMode = false
                )
                StepEntryTile(
                    stepDescription = "To do something",
                    onDescriptionValueChanged = {},
                    onConfirmClick = { },
                    onCancelClick = { },
                )

                NewStepTile(text = "Add step".uppercase(), icon = Icons.TwoTone.Add, onClick = { /*TODO*/ })
            }
        }
    }
}