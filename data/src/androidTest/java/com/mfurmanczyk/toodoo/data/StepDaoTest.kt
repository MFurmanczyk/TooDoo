package com.mfurmanczyk.toodoo.data

import android.content.Context
import android.util.Log
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mfurmanczyk.toodoo.data.database.TooDooDatabase
import com.mfurmanczyk.toodoo.data.database.dao.StepDao
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

private const val TAG = "StepDaoTest"

@RunWith(AndroidJUnit4::class)
class StepDaoTest {

    private lateinit var database: TooDooDatabase
    private lateinit var dao: StepDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = TooDooDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.stepDao()
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
    fun getAllSteps_readAllSteps_returnsNineRows() {
        runBlocking {
            val steps = dao.getAllSteps().first()

            Log.i(TAG, "getAllSteps_readAllSteps_returnsNineRows: rows - ${steps.size}")

            assertTrue(steps.size == 9)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getStepById_readFirstStepById_returnsCorrectStep() {
        runBlocking {
            val step = dao.getStepById(1).first()

            Log.i(TAG, "getStepById_readFirstStepById_returnsCorrectStep: step description - ${step?.description}")

            assertEquals(TestData.StepData.steps.first(), step)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getStepById_readNonexistentTask_returnsNull() {
        runBlocking {
            val step = dao.getStepById(10).first()

            if(step == null) Log.i(TAG, "getStepById_readNonexistentTask_returnsNull: step is null")
            else Log.i(TAG, "getStepById_readNonexistentTask_returnsNull: step description - ${step.description}")

            assertNull(step)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertStep_insertNewStep_returnsTenRows_lastRowAsInsertedStep() {
        runBlocking {
            dao.insertStep(TestData.StepData.step_10_insert)

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "insertStep_insertNewStep_returnsTenRows_lastRowAsInsertedStep: rows - ${steps.size}")

            assertTrue(steps.size == 10)
            assertEquals(TestData.StepData.step_10_insert, steps.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertStep_insertDuplicateStep_returnsNineRows_taskNotInserted() {
        runBlocking {
            dao.insertStep(TestData.StepData.step_8_duplicate)

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "insertStep_insertDuplicateStep_returnsNineRows_taskNotInserted: rows - ${steps.size}")

            assertTrue(steps.size == 9)
            assertFalse(steps.contains(TestData.StepData.step_8_duplicate))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateStep_updateSecondStep_returnsNineRows_stepUpdated() {
        runBlocking {
            dao.updateStep(TestData.StepData.step_2_update)

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "updateStep_updateSecondStep_returnsNineRows_stepUpdated: rows - ${steps.size}")

            assertTrue(steps.size == 9)
            assertTrue(steps.contains(TestData.StepData.step_2_update))
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateStep_updateNonexistentStep_returnsNineRows_nothingHappens() {
        runBlocking {
            dao.updateStep(TestData.StepData.step_10_insert)

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "updateStep_updateNonexistentStep_returnsNineRows_nothingHappens: rows - ${steps.size}")

            assertTrue(steps.size == 9)
            assertFalse(steps.contains(TestData.StepData.step_10_insert))
        }
    }

    @Test
    @Throws(Exception::class)
    fun relation_deleteFirstTask_returnsSevenRows_removesFirstTwoSteps() {
        runBlocking {
            val taskDao = database.taskDao()
            taskDao.deleteTasks(TestData.TaskData.tasks.first())

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "relation_deleteFirstTask_returnsSevenRows_removesFirstTwoSteps: rows - ${steps.size}")

            assertTrue(steps.size == 7)
            assertFalse(steps.containsAll(listOf(TestData.StepData.steps[0], TestData.StepData.steps[1])))
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteStep_deleteFirstStep_returnsEightRows_stepDeleted() {
        runBlocking {
            dao.deleteStep(TestData.StepData.steps.first())

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "deleteStep_deleteFirstStep_returnsEightRows_stepDeleted: rows - ${steps.size}")

            assertFalse(steps.contains(TestData.StepData.steps.first()))
            assertTrue(steps.size == 8)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteStep_deleteNonexistentStep_returnsNineRows_noRowsDeleted() {
        runBlocking {
            dao.deleteStep(TestData.StepData.step_10_insert)
            
            val steps = dao.getAllSteps().first()

            Log.i(TAG, "deleteStep_deleteNonexistentStep_returnsNineRows_noRowsDeleted: rows - ${steps.size}")
            
            assertTrue(steps.size == 9)
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteSteps_deleteFirstTwoSteps_returnsSevenRows_firstTwoRowsDeleted() {
        runBlocking {
            dao.deleteSteps(TestData.StepData.steps[0], TestData.StepData.steps[1])

            val steps = dao.getAllSteps().first()

            Log.i(TAG, "deleteSteps_deleteFirstTwoSteps_returnsSevenRows_firstTwoRowsDeleted: rows - ${steps.size}")

            assertFalse(steps.containsAll(listOf(TestData.StepData.steps[0], TestData.StepData.steps[1])))
        }
    }
}