package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface TaskRepository {

    /**
     * Returns all tasks from datasource.
     */
    fun getAllTasks(): Flow<List<Task>>

    /**
     * Returns all tasks that have no category assigned from datasource.
     */
    fun getAllUncategorizedTasks(): Flow<List<Task>>

    /**
     * Returns single task with given [id] from datasource.
     */
    fun getTaskById(id: Long): Flow<Task>


    /**
     * Returns all tasks with given [isDone] parameter from datasource.
     */
    fun getTasksByIsDone(isDone: Boolean): Flow<List<Task>>

    /**
     * Returns all tasks with [Task.dueDate] before given [date] from datasource.
     */
    fun getTasksBeforeDate(date: LocalDate): Flow<List<Task>>

    /**
     * Returns all tasks with [Task.dueDate] after given [date] from datasource.
     */
    fun getTasksAfterDate(date: LocalDate): Flow<List<Task>>

    /**
     * Returns all tasks with [Task.dueDate] equals given [date] from datasource.
     */
    fun getTasksAtDate(date: LocalDate): Flow<List<Task>>

    /**
     * Returns all [Task]s with associated [Step]s. Wrapped in [TaskWithSteps] data class.
     */
    fun getAllTasksWithSteps(): Flow<List<TaskWithSteps>>

    /**
     * Returns [Task] with all associated [Step]s. Wrapped in [TaskWithSteps] data class.
     */
    fun getTaskWithSteps(task: Task): Flow<TaskWithSteps>

    /**
     * Adds [task] to datasource.
     */
    suspend fun addTask(task: Task)

    /**
     * Updates [task] in datasource.
     */
    suspend fun updateTask(task: Task)

    /**
     * Removes [task] from datasource.
     */
    suspend fun removeTask(task: Task)

    /**
     * Removes multiple [tasks] from datasource.
     */
    suspend fun removeTasks(vararg tasks: Task)
}