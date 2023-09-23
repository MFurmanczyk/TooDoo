package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.CategoryDao
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

@RunWith(MockitoJUnitRunner::class)
class RoomCategoryRepositoryTest {

    private lateinit var dataSource: TestData
    @Mock private lateinit var dao: CategoryDao
    private lateinit var repository: RoomCategoryRepository

    @Before
    fun initMock() {

        dataSource = TestData()

        dao = mock {
            on { getAllCategories() } doReturn flowOf(dataSource.categoryData.categories)
            on { getCategoryById(1) } doReturn flowOf(dataSource.categoryData.categories.first())
            on { getAllCategoriesWithTasks() } doReturn flowOf(dataSource.categoriesWithTasksData.categoriesWithTasks)
            on { getCategoryWithTasksById(1) } doReturn flowOf(dataSource.categoriesWithTasksData.categoriesWithTasks.first())
            onBlocking { insertCategory(dataSource.categoryData.category_3_insert)} doAnswer {dataSource.categoryData.addCategory() }
            onBlocking { updateCategory(dataSource.categoryData.category_2_update) } doAnswer { dataSource.categoryData.updateCategory() }
            onBlocking { deleteCategory(dataSource.categoryData.categories.first()) } doAnswer { dataSource.categoryData.deleteFirst() }
            onBlocking { deleteCategories(dataSource.categoryData.categories.first(), dataSource.categoryData.categories.last()) } doAnswer { dataSource.categoryData.deleteFirstAndLast() }
        }
        repository = RoomCategoryRepository(dao)
    }

    @Test
    @Throws(Exception::class)
    fun getAllCategories_returnsTwoCategories() {
        runBlocking {
            val categories = repository.getAllCategories().first()

            println(categories.size)
            assertTrue(categories.size == 2)
            assertEquals(dataSource.categoryData.categories.first(), categories.first())
            assertEquals(dataSource.categoryData.categories.last(), categories.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun getCategoryById_returnsFirstCategory() {
        runBlocking {
            val category = repository.getCategoryById(1).first()

            assertNotNull(category)
            assertEquals(dataSource.categoryData.categories.first(), category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getAllCategoriesWithTasks_returnsTwoCategories_firstCategoryWithOneTask() {
        runBlocking {
            val categoriesWithTasks = repository.getAllCategoriesWithTasks().first()

            assertTrue(categoriesWithTasks.size == 2)
            assertTrue(categoriesWithTasks.first().tasks.size == 1)
            assertEquals(categoriesWithTasks.first().tasks.first(), dataSource.taskData.tasks.first())
            assertEquals(categoriesWithTasks.first().category, dataSource.categoriesWithTasksData.categoriesWithTasks.first().category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun getCategoryWithTasks_returnsFirstCategory_categoryWithOneTask() {
        runBlocking {
            val categoryWithTasks = repository.getCategoryWithTaskById(dataSource.categoryData.categories.first().id).first()

            assertTrue(categoryWithTasks?.tasks?.size == 1)
            assertEquals(categoryWithTasks?.tasks?.first(), dataSource.taskData.tasks.first())
            assertEquals(categoryWithTasks?.category, dataSource.categoriesWithTasksData.categoriesWithTasks.first().category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun addCategory_addsNewCategory() {
        runBlocking {
            repository.addCategory(dataSource.categoryData.category_3_insert)

            val categories = repository.getAllCategories().first()

            assertTrue(categories.size == 3)
            assertEquals(dataSource.categoryData.category_3_insert, categories.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateCategory_updatesSecondCategory() {
        runBlocking {
            repository.updateCategory(dataSource.categoryData.category_2_update)

            val categories = repository.getAllCategories().first()

            assertTrue(categories.size == 2)
            assertTrue(categories.contains(dataSource.categoryData.category_2_update))
            assertFalse(categories.contains(dataSource.categoryData.last()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeCategory_removesFirstCategory() {
        runBlocking {
            repository.removeCategory(dataSource.categoryData.first())

            val categories = repository.getAllCategories().first()

            assertTrue(categories.size == 1)
            assertFalse(categories.contains(dataSource.categoryData.first()))
        }
    }

    @Test
    @Throws(Exception::class)
    fun removeCategories_removesAllCategories() {
        runBlocking {
            repository.removeCategories(dataSource.categoryData.first(), dataSource.categoryData.last())

            val categories = repository.getAllCategories().first()

            assertTrue(categories.isEmpty())
        }
    }
}