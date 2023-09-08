package com.mfurmanczyk.toodoo.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mfurmanczyk.toodoo.data.database.TooDooDatabase
import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.test.TestData
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
import java.time.LocalDate

private const val TAG = "TaskDaoTest"

@RunWith(AndroidJUnit4::class)
class TaskDaoTest {

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
    fun readAllTasks_returnsSixRows_firstAndLastTaskCorrect() {
        runBlocking {
            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "readAllTasks_returnsSixRows_firstAndLastTaskCorrect: rows - ${tasks.size}")
            Log.i(TAG, "readAllTasks_returnsSixRows_firstAndLastTaskCorrect: firstElement - name: ${tasks.first().name}")
            Log.i(TAG, "readAllTasks_returnsSixRows_firstAndLastTaskCorrect: lastElement - name: ${tasks.last().name}")

            assertTrue(tasks.size == 6)
            assertEquals(TestData.TaskData.tasks[0], tasks.first())
            assertEquals(TestData.TaskData.tasks[5], tasks.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readAllUncategorizedTasks_returnsOneRow_elementCorrect() {
        runBlocking {
            val tasks = dao.getAllUncategorizedTasks().first()

            Log.i(TAG, "readAllUncategorizedTasks_returnsOneRow_elementCorrect: rows - ${tasks.size}")
            Log.i(TAG, "readAllUncategorizedTasks_returnsOneRow_elementCorrect: element - name: ${tasks.first().name}")

            assertTrue(tasks.size == 1)
            assertEquals(TestData.TaskData.tasks[5], tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readFirstTaskById_returnsCorrectTask() {
        runBlocking {
            val task = dao.getTaskById(1).first()

            Log.i(TAG, "readFirstTaskById_returnsCorrectTask: element - name: ${task?.name}")

            assertEquals(TestData.TaskData.tasks.first(), task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentTask_returnsNull() {
        runBlocking {
            val task = dao.getTaskById(7).first()

            if(task == null) Log.i(TAG, "readNonexistentTask_returnsNull: element - null")
            else Log.i(TAG, "readNonexistentTask_returnsNull: element - name: ${task.name}")

            assertNull(task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksByIsDoneTrue_returnsThreeRows() {
        runBlocking {
            val tasks = dao.getTasksByIsDone(true).first()

            Log.i(TAG, "readTasksByIsDoneTrue_returnsThreeRows: rows - ${tasks.size}")

            assertTrue(tasks.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksBeforeDate_2010_returnsTwoRows_firstElementCorrect() {
        runBlocking {
            val tasks = dao.getTasksBeforeDate(LocalDate.of(2010, 1, 1)).first()

            Log.i(TAG, "readTasksBeforeDate_2010_returnsTwoRows_firstElementCorrect: rows - ${tasks.size}")
            Log.i(TAG, "readTasksBeforeDate_2010_returnsTwoRows_firstElementCorrect: firstElement - name: ${tasks.first().name}")

            assertTrue(tasks.size == 2)
            assertEquals(TestData.TaskData.tasks[4], tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksBeforeDate_2008_returnsEmptyList() {
        runBlocking {
            val tasks = dao.getTasksBeforeDate(LocalDate.of(2008, 1, 1)).first()

            Log.i(TAG, "readTasksBeforeDate_2008_returnsTwoRows: rows - ${tasks.size}")

            assertTrue(tasks.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksAfterDate_2010_returnsFourRows_firstElementCorrect() {
        runBlocking {
            val tasks = dao.getTasksAfterDate(LocalDate.of(2010, 1, 1)).first()

            Log.i(TAG, "readTasksAfterDate_2010_returnsFourRows_firstElementCorrect: rows - ${tasks.size}")
            Log.i(TAG, "readTasksAfterDate_2010_returnsFourRows_firstElementCorrect: firstElement - name: ${tasks.first().name}")

            assertTrue(tasks.size == 4)
            assertEquals(TestData.TaskData.tasks.first(), tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksAfterDate_2012_returnsEmptyList() {
        runBlocking {
            val tasks = dao.getTasksAfterDate(LocalDate.of(2012, 1, 1)).first()

            Log.i(TAG, "readTasksAfterDate_2012_returnsEmptyList: rows - ${tasks.size}")

            assertTrue(tasks.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksAtDate_2010_returnsTwoRows_firstElementCorrect() {
        runBlocking {
            val tasks = dao.getTasksByDate(LocalDate.of(2010, 1,3)).first()

            Log.i(TAG, "readTasksAtDate_2010_returnsTwoRows_firstElementCorrect: rows - ${tasks.size}")
            Log.i(TAG, "readTasksAtDate_2010_returnsTwoRows_firstElementCorrect: firstElement - name: ${tasks.first().name}")

            assertTrue(tasks.size == 2)
            assertEquals(TestData.TaskData.tasks.first(), tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readTasksAtDate_badDate_returnsEmptyList() {
        runBlocking {
            val tasks = dao.getTasksByDate(LocalDate.of(2014, 1,1)).first()

            Log.i(TAG, "readTasksAtDate_2010_returnsTwoRows_firstElementCorrect: rows - ${tasks.size}")

            assertTrue(tasks.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps() {
        runBlocking {
            val tasksWithSteps = dao.getAllTasksWithSteps().first()

            Log.i(TAG, "readAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps: size - ${tasksWithSteps.size}")
            Log.i(TAG, "readAllTasksWithSteps_returnsSixRows_firstTaskWithTwoSteps: steps in first task - ${tasksWithSteps.first().steps.size}")

            assertTrue(tasksWithSteps.size == 6)
            assertTrue(tasksWithSteps.first().steps.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readSecondTaskWithSteps_returnsTaskWithThreeSteps() {
        runBlocking {
            val taskWithSteps = dao.getTaskWithStepsById(2).first()

            Log.i(TAG, "readSecondTaskWithSteps_returnsTaskWithThreeSteps: task name - ${taskWithSteps?.task?.name}")
            Log.i(TAG, "readSecondTaskWithSteps_returnsTaskWithThreeSteps: step rows - ${taskWithSteps?.steps?.size}")

            assertTrue((taskWithSteps?.steps?.size ?: 0) == 3)
            assertEquals(TestData.TaskData.tasks[1], taskWithSteps?.task)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentTaskWithSteps_returnsNull() {
        runBlocking {
            val taskWithSteps = dao.getTaskWithStepsById(7).first()

            if(taskWithSteps == null) Log.i(TAG, "readNonexistentTaskWithSteps_returnsNull: null")
            else Log.i(TAG, "readNonexistentTaskWithSteps_returnsNull: task name - ${taskWithSteps.task.name}")

            assertNull(taskWithSteps)
        }
    }

    @Test
    @Throws
    fun deleteFirstCategory_foreignKeyInFirstTaskNull() {
        runBlocking {
            val categoryDao = database.categoryDao()
            categoryDao.deleteCategory(TestData.CategoryData.categories.first())

            val task = dao.getTaskById(1).first()

            if(task?.categoryId == null) Log.i(TAG, "deleteFirstCategory_foreignKeyInFirstTaskNull: category is null")
            else Log.i(TAG, "deleteFirstCategory_foreignKeyInFirstTaskNull: category_id - ${task.categoryId}")

            assertNull(task?.categoryId)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertNewTask_returnsSeverRows_lastElementAsNewTask() {
        runBlocking {
            dao.insertTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "insertNewTask_returnsSeverRows_lastElementAsNewTask: rows - ${tasks.size}")
            Log.i(TAG, "insertNewTask_returnsSeverRows_lastElementAsNewTask: lastElement - name: ${tasks.last().name}")

            assertTrue(tasks.size == 7)
            assertEquals(TestData.TaskData.task_7_insert, tasks.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertDuplicatedTask_returnsSixRows_noNewRowInserted() {
        runBlocking {
            dao.insertTask(TestData.TaskData.task_6_duplicate)
            
            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "insertDuplicatedTask_returnsSixRows_nothingHappens: rows - ${tasks.size}")

            assertFalse(tasks.contains(TestData.TaskData.task_6_duplicate))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateSixthTask_updateSuccessful() {
        runBlocking {
            dao.updateTask(TestData.TaskData.task_6_update)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "updateSixthTask_updateSuccessful: updated task - name: ${tasks[5].name}")

            assertTrue(tasks.contains(TestData.TaskData.task_6_update))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistentTask_nothingHappens() {
        runBlocking {
            dao.updateTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "updateNonexistentTask_nothingHappens: rows - ${tasks.size}")

            assertFalse(tasks.contains(TestData.TaskData.task_7_insert))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteFirstTask_returnsFiveRowsWithoutFirstTask() {
        runBlocking {
            dao.deleteTask(TestData.TaskData.tasks.first())

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteFirstTask_returnsFiveRowsWithoutFirstTask: rows - ${tasks.size}")
            Log.i(TAG, "deleteFirstTask_returnsFiveRowsWithoutFirstTask: firstElement - name: ${tasks.first().name}")

            assertTrue(tasks.size == 5)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
            assertEquals(TestData.TaskData.tasks[1], tasks.first())
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistentTask_nothingHappens() {
        runBlocking {
            dao.deleteTask(TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteNonexistentTask_nothingHappens: rows - ${tasks.size}")

            assertEquals(tasks, TestData.TaskData.tasks)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTwoTasks_returnsFourRows_deletesFirstAndLastTask() {
        runBlocking {
            dao.deleteTasks(TestData.TaskData.tasks.first(), TestData.TaskData.tasks.last())

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTwoTasks_returnsFourRows_deletesFirstAndLastTask: rows - ${tasks.size}")

            assertTrue(tasks.size == 4)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
            assertFalse(tasks.contains(TestData.TaskData.tasks.last()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteTwoTasks_returnsFiveRows_deletesFirstAndNonexistentTasks() {
        runBlocking {
            dao.deleteTasks(TestData.TaskData.tasks.first(), TestData.TaskData.task_7_insert)

            val tasks = dao.getAllTasks().first()

            Log.i(TAG, "deleteTwoTasks_returnsFiveRows_deletesFirstAndNonexistentTasks: rows - ${tasks.size}")

            assertTrue(tasks.size == 5)
            assertFalse(tasks.contains(TestData.TaskData.tasks.first()))
        }
    }

}