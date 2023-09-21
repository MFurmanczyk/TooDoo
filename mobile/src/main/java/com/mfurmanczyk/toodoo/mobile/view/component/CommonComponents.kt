package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing

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
        }
    }

}