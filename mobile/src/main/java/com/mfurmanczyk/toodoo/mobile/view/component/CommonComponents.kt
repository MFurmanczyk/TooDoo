package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
            Text(text = task.name)
            Spacer(modifier = Modifier.weight(1F))
            Text(
                text = task.dueDate.format(DateTimeFormatter.ofPattern("E, dd MMM yyyy")),
                style = MaterialTheme.typography.labelSmall
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