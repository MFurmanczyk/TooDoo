package com.mfurmanczyk.toodoo.data.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task


data class TaskWithSteps(
    @Embedded val task: Task,
    @Relation(
        parentColumn = "id",
        entityColumn = "taskId"
    )
    val steps: List<Step>
)
