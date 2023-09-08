package com.mfurmanczyk.toodoo.data

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mfurmanczyk.toodoo.data.database.TooDooDatabase
import com.mfurmanczyk.toodoo.data.database.dao.CategoryDao
import com.mfurmanczyk.toodoo.data.test.TestData
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CategoryDaoTest {

    private lateinit var database: TooDooDatabase
    private lateinit var dao: CategoryDao

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        database = Room.inMemoryDatabaseBuilder(
            context = context,
            klass = TooDooDatabase::class.java
        )
            .allowMainThreadQueries()
            .build()

        dao = database.categoryDao()
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
    fun readAllCategoriesFromDb_returnsTwoRows() {

        runBlocking {
            val categories = dao.getAllCategories().first()

            assertEquals(TestData.CategoryData.categories[0], categories[0])
            assertEquals(TestData.CategoryData.categories[1], categories[1])
            assertTrue(categories.size == 2)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readFirstCategoryFromDb_returnsOneRow() {
        runBlocking {
            val category = dao.getCategoryById(1).first()

            assertEquals(TestData.CategoryData.categories[0], category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentCategoryFromDb_returnsNull() {
        runBlocking {
            val category = dao.getCategoryById(3).first()

            assertNull(category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask() {
        runBlocking {
            val categoriesWithTasks = dao.getAllCategoriesWithTasks().first()

            assertTrue(categoriesWithTasks.size == 2)
            assertTrue(categoriesWithTasks[0].tasks.size == 1)
            assertEquals(TestData.CategoryData.categories[0], categoriesWithTasks[0].category)
            assertEquals(TestData.TaskData.tasks[0], categoriesWithTasks[0].tasks[0])
        }
    }

    @Test
    @Throws(Exception::class)
    fun readSecondCategoryWithTasks_categoryWithFourTasks_firstAndLastTaskCorrect() {
        runBlocking {
            val categoryWithTasks = dao.getCategoryWithTasksById(2).first()

            assertTrue(categoryWithTasks.tasks.size == 4)
            assertEquals(TestData.CategoryData.categories[1], categoryWithTasks.category)
            assertEquals(TestData.TaskData.tasks[1], categoryWithTasks.tasks[0])
            assertEquals(TestData.TaskData.tasks[4], categoryWithTasks.tasks[3])
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentCategoryWithTasks_returnsNull() {
        runBlocking {
            val categoryWithTasks = dao.getCategoryWithTasksById(3).first()

            assertNull(categoryWithTasks)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertNewCategory_lastElementAsNewCategory() {
        runBlocking {
            dao.insertCategory(TestData.CategoryData.category_3_insert)

            val categories = dao.getAllCategories().first()

            assertEquals(TestData.CategoryData.category_3_insert.copy(id = 3), categories.last())
            assertTrue(categories.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertDuplicateCategory_lastElementAsFirstInserted() {
        runBlocking {
            dao.insertCategory(TestData.CategoryData.category_3_insert)
            dao.insertCategory(TestData.CategoryData.category_3_duplicate)

            val categories = dao.getAllCategories().first()

            assertEquals(TestData.CategoryData.category_3_insert.copy(id = 3), categories.last())
            assertTrue(categories.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateSecondCategory_updateSuccessful() {
        runBlocking {
            dao.updateCategory(TestData.CategoryData.category_2_update)

            val category = dao.getCategoryById(2).first()

            assertEquals(TestData.CategoryData.category_2_update, category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistentCategory_nothingHappens() {
        runBlocking {
            dao.updateCategory(TestData.CategoryData.category_3_insert)

            val categories = dao.getAllCategories().first()

            assertTrue(categories.size == 2)
            assertEquals(TestData.CategoryData.categories[0], categories[0])
            assertEquals(TestData.CategoryData.categories[1], categories[1])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteFirstCategory_returnsOnlySecondCategory() {
        runBlocking {
            dao.deleteCategory(TestData.CategoryData.categories[0])

            val categories = dao.getAllCategories().first()

            assertTrue(categories.size == 1)
            assertEquals(TestData.CategoryData.categories[1], categories[0])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistentCategory_nothingHappens() {
        runBlocking {
            dao.deleteCategory(TestData.CategoryData.category_3_insert)

            val categories = dao.getAllCategories().first()

            assertTrue(categories.size == 2)
            assertEquals(TestData.CategoryData.categories[0], categories[0])
            assertEquals(TestData.CategoryData.categories[1], categories[1])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllCategories_returnsEmptyList() {
        runBlocking {
            dao.deleteCategories(TestData.CategoryData.categories[0], TestData.CategoryData.categories[1])

            val categories = dao.getAllCategories().first()

            assertTrue(categories.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteMultipleCategories_nonexistentAndFirstCategory_returnsListWithSecondCategory() {
        runBlocking {
            dao.deleteCategories(TestData.CategoryData.categories[0], TestData.CategoryData.category_3_insert)

            val categories = dao.getAllCategories().first()

            assertTrue(categories.size == 1)
            assertEquals(TestData.CategoryData.categories[1], categories[0])
        }
    }
}