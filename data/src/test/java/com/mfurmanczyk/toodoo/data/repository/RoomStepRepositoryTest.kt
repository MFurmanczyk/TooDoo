package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.StepDao
import com.mfurmanczyk.toodoo.data.fakedata.TestData
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.doAnswer
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock

@RunWith(MockitoJUnitRunner::class)
class RoomStepRepositoryTest {

    private lateinit var dataSource: TestData
    @Mock private lateinit var dao: StepDao
    private lateinit var repository: RoomStepRepository

    @Before
    fun initMock() {

        dataSource = TestData()

        dao = mock {
            on { getAllSteps() } doReturn flowOf(dataSource.stepData.steps)
            on { getStepById(1) } doReturn flowOf(dataSource.stepData.steps.first())
            onBlocking { insertStep(dataSource.stepData.step_10_insert)} doAnswer { dataSource.stepData.addStep() }
            onBlocking { updateStep(dataSource.stepData.step_2_update) } doAnswer { dataSource.stepData.updateStep() }
            onBlocking { deleteStep(dataSource.stepData.steps.first()) } doAnswer { dataSource.stepData.deleteFirst() }
            onBlocking { deleteSteps(dataSource.stepData.steps.first(), dataSource.stepData.steps.last()) } doAnswer { dataSource.stepData.deleteFirstAndLast() }
        }

        repository = RoomStepRepository(dao)
    }

    @Test
    @Throws(Exception::class)
    fun getAllSteps_returnsNineSteps_firstAndLastStepCorrect() {
        runBlocking {
            val steps = repository.getAllSteps().first()

            assertTrue(steps.size == 9)
            assertEquals(dataSource.stepData.first(), steps.first())
            assertEquals(dataSource.stepData.last(), steps.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getStepById_returnsFirstStep() {
        runBlocking {
            val step = repository.getStepById(1).first()

            assertEquals(dataSource.stepData.first(), step)
        }
    }

    @Test
    @Throws(Exception::class)
    fun addStep_addNewStep_insertSuccessful() {
        runBlocking {
            repository.addStep(dataSource.stepData.step_10_insert)

            val steps = repository.getAllSteps().first()

            assertTrue(steps.contains(dataSource.stepData.step_10_insert))
            assertTrue(steps.size == 10)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateStep_updatesSecondStep_updateSuccessful() {
        runBlocking {
            repository.updateStep(dataSource.stepData.step_2_update)

            val steps = repository.getAllSteps().first()

            assertTrue(steps.contains(dataSource.stepData.step_2_update))
            assertTrue(steps.size == 9)
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeStep_removesFirstStep_removeSuccessful() {
        runBlocking {
            repository.removeStep(dataSource.stepData.steps.first())

            val steps = repository.getAllSteps().first()

            assertTrue(steps.size == 8)
            assertFalse(steps.contains(dataSource.stepData.first()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeSteps_removesFirstAndLastStep_removeSuccessful() {
        runBlocking {
            repository.removeSteps(dataSource.stepData.steps.first(), dataSource.stepData.steps.last())

            val steps = repository.getAllSteps().first()

            assertTrue(steps.size == 7)
            assertFalse(steps.contains(dataSource.stepData.first()))
            assertFalse(steps.contains(dataSource.stepData.last()))
        }
    }
}