package com.mfurmanczyk.toodoo.data.repository

import android.util.Log
import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.fakedata.TestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

private const val TAG = "RoomTaskRepositoryTest"

@RunWith(MockitoJUnitRunner::class)
class RoomTaskRepositoryTest {

    private lateinit var dataSource: TestData
    @Mock private lateinit var dao: TaskDao
    private lateinit var repository: RoomTaskRepository

    @Before
    fun initMock() {

        dataSource = TestData()

        dao = mock {
            on { getAllTasks() } doReturn flowOf(dataSource.taskData.tasks)
            on { getAllUncategorizedTasks() } doReturn flowOf(dataSource.taskData.tasks.filter { it.categoryId == null })
            on { getTaskById(1) } doReturn flowOf(dataSource.taskData.tasks.first())
            on { getAllTasksWithSteps() } doReturn flowOf(dataSource.tasksWithStepsData.tasksWithSteps)
            on { getTaskWithStepsById(1)} doReturn flowOf(dataSource.tasksWithStepsData.tasksWithSteps.first())
            onBlocking { insertTask(dataSource.taskData.task_7_insert)} doAnswer { dataSource.taskData.addTask() }
            onBlocking { updateTask(dataSource.taskData.task_6_update) } doAnswer { dataSource.taskData.updateTask() }
            onBlocking { deleteTask(dataSource.taskData.tasks.first()) } doAnswer { dataSource.taskData.deleteFirst() }
            onBlocking { deleteTasks(dataSource.taskData.tasks.first(), dataSource.taskData.tasks.last()) } doAnswer { dataSource.taskData.deleteFirstAndLast() }
        }

        repository = RoomTaskRepository(dao)
    }

    @Test
    @Throws(Exception::class)
    fun getAllTasks_returnsSixRows_firstAndLastTaskCorrect() {
        runBlocking {
            val tasks = repository.getAllTasks().first()

            Log.i(TAG, "getAllTasks_returnsSixRows_firstAndLastTaskCorrect: rows - ${tasks.size}")

            assertEquals(dataSource.taskData.tasks.first(), tasks.first())
            assertEquals(dataSource.taskData.tasks.last(), tasks.last())
            assertTrue(tasks.size == 6)
        }

    }

    @Test
    @Throws(Exception::class)
    fun getAllUncategorizedTasks_returnsOneRow_taskCorrect() {
        runBlocking {
            val tasks = repository.getAllUncategorizedTasks().first()

            Log.i(TAG, "getAllUncategorizedTasks_returnsOneRow_taskCorrect: rows - ${tasks.size}")

            assertTrue(tasks.size == 1)
            assertEquals(dataSource.taskData.tasks.last(), tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskById_getFirstTask_returnsFirstTask() {
        runBlocking {
            val task = repository.getTaskById(1).first()

            Log.i(TAG, "getTaskById_getFirstTask_returnsFirstTask: test name - ${task?.name}")

            assertNotNull(task)
            assertEquals(dataSource.taskData.tasks.first(), task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllTasksWithSteps_returnsTwoRows_firstTaskWithTwoSteps() {
        runBlocking {
            val tasksWithSteps = repository.getAllTasksWithSteps().first()

            Log.i(TAG, "getAllTasksWithSteps_returnsTwoRows_firstTaskWithTwoSteps: rows - ${tasksWithSteps.size}")
            Log.i(TAG, "getAllTasksWithSteps_returnsTwoRows_firstTaskWithTwoSteps: steps in first task - ${tasksWithSteps.first().steps.size}")

            assertTrue(tasksWithSteps.size == 2)
            assertTrue(tasksWithSteps.first().steps.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskWithSteps_getFirstTaskWithStep_taskWithTwoSteps() {
        runBlocking {
            val taskWithSteps = repository.getTaskWithSteps(dataSource.taskData.tasks.first()).first()

            Log.i(TAG, "getTaskWithSteps_getFirstTaskWithStep_taskWithTwoSteps: steps in task - ${taskWithSteps?.steps?.size}")

            assertEquals(dataSource.taskData.tasks.first(), taskWithSteps?.task)
            assertTrue(taskWithSteps?.steps?.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun addTask_addsNewTask_insertSuccessful() {
        runBlocking {
            repository.addTask(dataSource.taskData.task_7_insert)

            val tasks = repository.getAllTasks().first()

            Log.i(TAG, "addTask_addsNewTask_insertSuccessful: rows - ${tasks.size}")

            assertTrue(tasks.size == 7)
            assertTrue(tasks.contains(dataSource.taskData.task_7_insert))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateTask_updateSixthTask_updateSuccessful() {
        runBlocking {
            repository.updateTask(dataSource.taskData.task_6_update)

            val tasks = repository.getAllTasks().first()

            Log.i(TAG, "updateTask_taskUpdatedSuccessfully: rows = ${tasks.size}")

            assertTrue(tasks.contains(dataSource.taskData.task_6_update))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeTask_removeFirstTask_taskRemoved() {
        runBlocking {
            repository.removeTask(dataSource.taskData.tasks.first())

            val tasks = repository.getAllTasks().first()

            Log.i(TAG, "deleteTask_removeFirstTask_taskRemoved: rows - ${tasks.size}")

            assertTrue(tasks.size == 5)
            assertFalse(tasks.contains(dataSource.taskData.first()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeTasks_removeFirstAndLastTask_tasksRemoved() {
        runBlocking {
            repository.removeTasks(dataSource.taskData.tasks.first(), dataSource.taskData.tasks.last())

            val tasks = repository.getAllTasks().first()

            Log.i(TAG, "deleteTasks_removeFirstAndLastTask_tasksRemoved: rows - ${tasks.size}")

            assertTrue(tasks.size == 4)
            assertFalse(tasks.contains(dataSource.taskData.first()))
            assertFalse(tasks.contains(dataSource.taskData.last()))
        }
    }

}