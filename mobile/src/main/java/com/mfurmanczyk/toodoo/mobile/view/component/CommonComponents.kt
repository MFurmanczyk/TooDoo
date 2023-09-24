package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Composable
fun TaskTile(
    onClick: (Task) -> Unit,
    onCheckboxClick: (Task, Boolean) -> Unit,
    task: Task,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier
            .fillMaxWidth(),
        onClick = {
            onClick(task)
        },
        shape = MaterialTheme.shapes.medium,
        shadowElevation = 12.dp
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = task.isDone,
                onCheckedChange = {
                    onCheckboxClick(task, it)
                }
            )
            Text(
                modifier = Modifier.weight(3F),
                text = task.name,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                modifier = Modifier.weight(1F),
                text = task.dueDate.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy")),
                style = MaterialTheme.typography.labelSmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun ConfirmationDialog(
    onDismissRequest: () -> Unit,
    onConfirmation: () -> Unit,
    dialogTitle: String,
    dialogText: String,
    icon: ImageVector,
) {
    AlertDialog(
        icon = {
            Icon(icon, contentDescription = null)
        },
        title = {
            Text(text = dialogTitle)
        },
        text = {
            Text(text = dialogText)
        },
        onDismissRequest = {
            onDismissRequest()
        },
        confirmButton = {
            TextButton(
                onClick = {
                    onConfirmation()
                }
            ) {
                Text(stringResource(R.string.confirm).uppercase())
            }
        },
        dismissButton = {
            TextButton(
                onClick = {
                    onDismissRequest()
                }
            ) {
                Text(stringResource(R.string.cancel).uppercase())
            }
        }
    )
}

@Composable
fun InputField(
    label: String,
    value: String,
    onValueChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    maxLines: Int = 1,
    singleLine:Boolean = true,
    readOnly: Boolean = false,
    trailingIcon: @Composable (() -> Unit) = {}
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChanged,
        label = {
            Text(text = label)
        },
        maxLines = maxLines,
        shape = MaterialTheme.shapes.medium,
        singleLine = singleLine,
        readOnly = readOnly,
        trailingIcon = trailingIcon
    )
}

@Composable
fun StepTile(
    step: Step,
    onStepChecked: (Step, Boolean) -> Unit,
    onRemoveClick: (Step) -> Unit,
    editMode: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = MaterialTheme.shapes.medium
    ) {
        Row(
            modifier = Modifier.padding(MaterialTheme.spacing.small),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(!editMode) {
                Checkbox(
                    checked = step.isDone,
                    onCheckedChange = {
                        onStepChecked(step, it)
                    }
                )
            }
            Text(
                text = step.description,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = modifier.padding(start = if(editMode) MaterialTheme.spacing.small else MaterialTheme.spacing.default)
            )
            if(editMode && step.isDone) Icon(imageVector = Icons.TwoTone.Check, contentDescription = null)
            Spacer(modifier = Modifier.weight(1F))
            if(editMode) {
                IconButton(
                    onClick = { onRemoveClick(step) }
                ) {
                    Icon(imageVector = Icons.TwoTone.Delete, contentDescription = null)
                }
            }
        }
    }
}

@Preview
@Composable
fun TaskTilePreview() {
    TooDooTheme {
        TaskTile(
            onClick = {

            },
            onCheckboxClick = { _, _ ->

            },
            task = Task(
                id = 1,
                categoryId = 1,
                name = "Pay rent for apartment",
                description = null,
                createdOn = LocalDateTime.of(2010, 1, 1, 12, 0, 0),
                completedOn = LocalDateTime.of(2010, 1, 2, 12, 0, 0),
                dueDate = LocalDate.of(2023, 1, 3),
                isDone = true
            )
        )
    }
}