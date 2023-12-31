package com.mfurmanczyk.toodoo.mobile.util

import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks

fun CategoryWithTasks.getCompletedTasksRatio(): Float {
    val doneTasksNum = this.tasks.filter {
        it.isDone
    }.size
    return if(tasks.isEmpty()) 0.toFloat() else doneTasksNum.toFloat() / tasks.size.toFloat()
}