package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject

class RoomTaskRepository @Inject constructor(private val dao: TaskDao): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override fun getAllUncategorizedTasks(): Flow<List<Task>> = dao.getAllUncategorizedTasks()

    override fun getTaskById(id: Long): Flow<Task?> = dao.getTaskById(id)

    override fun getTasksByIsDone(isDone: Boolean): Flow<List<Task>> = dao.getTasksByIsDone(isDone)

    override fun getTasksBeforeDate(date: LocalDate): Flow<List<Task>> = dao.getTasksBeforeDate(date)

    override fun getTasksAfterDate(date: LocalDate): Flow<List<Task>> = dao.getTasksAfterDate(date)

    override fun getTasksAtDate(date: LocalDate): Flow<List<Task>> = dao.getTasksAtDate(date)

    override fun getAllTasksWithSteps(): Flow<List<TaskWithSteps>> = dao.getAllTasksWithSteps()

    override fun getTaskWithSteps(task: Task): Flow<TaskWithSteps?> = dao.getTaskWithStepsById(task.id)

    override suspend fun addTask(task: Task) = dao.insertTask(task)

    override suspend fun updateTask(task: Task) = dao.updateTask(task)

    override suspend fun removeTask(task: Task) = dao.deleteTask(task)

    override suspend fun removeTasks(vararg tasks: Task) = dao.deleteTasks(tasks = tasks)
}