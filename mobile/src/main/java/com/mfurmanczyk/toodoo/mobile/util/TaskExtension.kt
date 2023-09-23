package com.mfurmanczyk.toodoo.mobile.util

import com.mfurmanczyk.toodoo.data.model.Task

/**
 * Returns progress of gives sublist of tasks
 */
fun List<Task>.progress(): Float {
    val allTasksCount = size
    val doneTasksCount = filter { it.isDone }.size

    return doneTasksCount.toFloat()/allTasksCount.toFloat()
}