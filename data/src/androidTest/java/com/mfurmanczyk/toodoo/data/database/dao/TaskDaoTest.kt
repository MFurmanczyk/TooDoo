package com.mfurmanczyk.toodoo.data.database.dao

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mfurmanczyk.toodoo.data.database.TooDooDatabase
import com.mfurmanczyk.toodoo.data.fakedata.TestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

private const val TAG = "TaskDaoTest"

@RunWith(AndroidJUnit4::class)
internal class TaskDaoTest {

    private lateinit var database: TooDooDatabase
    private lateinit var dao: TaskDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = TooDooDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.taskDao()
        initDb()

    }

    private fun initDb() {
        val categoryDao = database.categoryDao()
        val taskDao = database.taskDao()
        val stepDao = database.stepDao()

        runBlocking {
            TestData.CategoryData.categories.forEach { category ->
                categoryDao.insertCategory(category)
            }

            TestData.TaskData.tasks.forEach { task ->
                taskDao.insertTask(task)
            }

            TestData.StepData.steps.forEach { step ->
                stepDao.insertStep(step)
            }
        }
    }


    @After
    @Throws(IOException::class)
    fun closeDb() {
        database.close()
    }

    @Test
    @Throws(Exception::class)
    fun getAllTasks_returnsSixRows_firstAndLastTaskCorrect() {
        runBlocking {
            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "getAllTasks_returnsSixRows_firstAndLastTaskCorrect: rows - ${tasks.size}")
            Log.i(TAG, "getAllTasks_returnsSixRows_firstAndLastTaskCorrect: firstElement - name: ${tasks.first().name}")
            Log.i(TAG, "getAllTasks_returnsSixRows_firstAndLastTaskCorrect: lastElement - name: ${tasks.last().name}")

            assertTrue(tasks.size == 6)
            assertEquals(TestData.TaskData.tasks[0], tasks.first())
            assertEquals(TestData.TaskData.tasks[5], tasks.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllUncategorizedTasks_returnsOneRow_elementCorrect() {
        runBlocking {
            val tasks = dao.getAllUncategorizedTasks().first()

            Log.i(TAG, "getAllUncategorizedTasks_returnsOneRow_elementCorrect: rows - ${tasks.size}")
            Log.i(TAG, "getAllUncategorizedTasks_returnsOneRow_elementCorrect: element - name: ${tasks.first().name}")

            assertTrue(tasks.size == 1)
            assertEquals(TestData.TaskData.tasks[5], tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskById_readFirstTaskById_returnsCorrectTask() {
        runBlocking {
            val task = dao.getTaskById(1).first()

            Log.i(TAG, "getTaskById_readFirstTaskById_returnsCorrectTask: element - name: ${task?.name}")

            assertEquals(TestData.TaskData.tasks.first(), task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskById_readNonexistentTask_returnsNull() {
        runBlocking {
            val task = dao.getTaskById(7).first()

            if(task == null) Log.i(TAG, "getTaskById_readNonexistentTask_returnsNull: element - null")
            else Log.i(TAG, "getTaskById_readNonexistentTask_returnsNull: element - name: ${task.name}")

            assertNull(task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps() {
        runBlocking {
            val tasksWithSteps = dao.getAllTasksWithSteps().first()

            Log.i(TAG, "getAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps: size - ${tasksWithSteps.size}")
            Log.i(TAG, "getAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps: steps in first task - ${tasksWithSteps.first().steps.size}")

            assertTrue(tasksWithSteps.size == 6)
            assertTrue(tasksWithSteps.first().steps.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskWithStepsById_readSecondTaskWithSteps_returnsTaskWithThreeSteps() {
        runBlocking {
            val taskWithSteps = dao.getTaskWithStepsById(2).first()

            Log.i(TAG, "getTaskWithStepsById_readSecondTaskWithSteps_returnsTaskWithThreeSteps: task name - ${taskWithSteps?.task?.name}")
            Log.i(TAG, "getTaskWithStepsById_readSecondTaskWithSteps_returnsTaskWithThreeSteps: step rows - ${taskWithSteps?.steps?.size}")

            assertTrue((taskWithSteps?.steps?.size ?: 0) == 3)
            assertEquals(TestData.TaskData.tasks[1], taskWithSteps?.task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getTaskWithStepsById_readNonexistentTaskWithSteps_returnsNull() {
        runBlocking {
            val taskWithSteps = dao.getTaskWithStepsById(7).first()

            if(taskWithSteps == null) Log.i(TAG, "getTaskWithStepsById_readNonexistentTaskWithSteps_returnsNull: null")
            else Log.i(TAG, "getTaskWithStepsById_readNonexistentTaskWithSteps_returnsNull: task name - ${taskWithSteps.task.name}")

            assertNull(taskWithSteps)
        }
    }

    @Test
    @Throws
    fun relation_deleteFirstCategory_foreignKeyInFirstTaskNull() {
        runBlocking {
            val categoryDao = database.categoryDao()
            categoryDao.deleteCategory(TestData.CategoryData.categories.first())

            val task = dao.getTaskById(1).first()

            if(task?.categoryId == null) Log.i(TAG, "relation_deleteFirstCategory_foreignKeyInFirstTaskNull: category is null")
            else Log.i(TAG, "relation_deleteFirstCategory_foreignKeyInFirstTaskNull: category_id - ${task.categoryId}")

            assertNull(task?.categoryId)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertTask_insertNewTask_returnsSeverRows_lastElementAsNewTask() {
        runBlocking {
            dao.insertTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "insertTask_insertNewTask_returnsSeverRows_lastElementAsNewTask: rows - ${tasks.size}")
            Log.i(TAG, "insertTask_insertNewTask_returnsSeverRows_lastElementAsNewTask: lastElement - name: ${tasks.last().name}")

            assertTrue(tasks.size == 7)
            assertEquals(TestData.TaskData.task_7_insert, tasks.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertTask_insertDuplicatedTask_returnsSixRows_noNewRowInserted() {
        runBlocking {
            dao.insertTask(TestData.TaskData.task_6_duplicate)
            
            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "insertTask_insertDuplicatedTask_returnsSixRows_noNewRowInserted: rows - ${tasks.size}")

            assertFalse(tasks.contains(TestData.TaskData.task_6_duplicate))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateTask_updateSixthTask_updateSuccessful() {
        runBlocking {
            dao.updateTask(TestData.TaskData.task_6_update)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "updateTask_updateSixthTask_updateSuccessful: updated task - name: ${tasks[5].name}")

            assertTrue(tasks.contains(TestData.TaskData.task_6_update))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateTask_updateNonexistentTask_nothingHappens() {
        runBlocking {
            dao.updateTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "updateTask_updateNonexistentTask_nothingHappens: rows - ${tasks.size}")

            assertFalse(tasks.contains(TestData.TaskData.task_7_insert))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask_deleteFirstTask_returnsFiveRowsWithoutFirstTask() {
        runBlocking {
            dao.deleteTask(TestData.TaskData.tasks.first())

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTask_deleteFirstTask_returnsFiveRowsWithoutFirstTask: rows - ${tasks.size}")
            Log.i(TAG, "deleteTask_deleteFirstTask_returnsFiveRowsWithoutFirstTask: firstElement - name: ${tasks.first().name}")

            assertTrue(tasks.size == 5)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
            assertEquals(TestData.TaskData.tasks[1], tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTask_deleteNonexistentTask_nothingHappens() {
        runBlocking {
            dao.deleteTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTask_deleteNonexistentTask_nothingHappens: rows - ${tasks.size}")

            assertEquals(tasks, TestData.TaskData.tasks)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTasks_deleteTwoTasks_returnsFourRows_deletesFirstAndLastTask() {
        runBlocking {
            dao.deleteTasks(TestData.TaskData.tasks.first(), TestData.TaskData.tasks.last())

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTasks_deleteTwoTasks_returnsFourRows_deletesFirstAndLastTask: rows - ${tasks.size}")

            assertTrue(tasks.size == 4)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
            assertFalse(tasks.contains(TestData.TaskData.tasks.last()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTasks_deleteTwoTasks_returnsFiveRows_deletesFirstAndNonexistentTasks() {
        runBlocking {
            dao.deleteTasks(TestData.TaskData.tasks.first(), TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTasks_deleteTwoTasks_returnsFiveRows_deletesFirstAndNonexistentTasks: rows - ${tasks.size}")

            assertTrue(tasks.size == 5)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
        }
    }

}