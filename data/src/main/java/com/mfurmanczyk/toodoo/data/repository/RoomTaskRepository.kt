package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomTaskRepository @Inject constructor(private val dao: TaskDao): TaskRepository {
    override fun getAllTasks(): Flow<List<Task>> = dao.getAllTasks()

    override fun getAllUncategorizedTasks(): Flow<List<Task>> = dao.getAllUncategorizedTasks()

    override fun getTaskById(id: Long): Flow<Task?> = dao.getTaskById(id)

    override fun getAllTasksWithSteps(): Flow<List<TaskWithSteps>> = dao.getAllTasksWithSteps()

    override fun getTaskWithStepsById(id: Long): Flow<TaskWithSteps?> = dao.getTaskWithStepsById(id)

    override suspend fun addTask(task: Task) = dao.insertTask(task)

    override suspend fun updateTask(task: Task) = dao.updateTask(task)

    override suspend fun removeTask(task: Task) = dao.deleteTask(task)

    override suspend fun removeTasks(vararg tasks: Task) = dao.deleteTasks(tasks = tasks)
}