package com.mfurmanczyk.toodoo.data

import android.content.Context
import android.util.Log
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

private const val TAG = "CategoryDaoTest"

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

            Log.i(TAG, "readAllCategoriesFromDb_returnsTwoRows: rows - ${categories.size}")
            Log.i(TAG, "readAllCategoriesFromDb_returnsTwoRows: firstElement - name: ${categories[0].name}")
            Log.i(TAG, "readAllCategoriesFromDb_returnsTwoRows: secondElement - name: ${categories[1].name}")

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

            Log.i(TAG, "readFirstCategoryFromDb_returnsOneRow: element - name: ${category?.name}")

            assertEquals(TestData.CategoryData.categories[0], category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentCategoryFromDb_returnsNull() {
        runBlocking {
            val category = dao.getCategoryById(3).first()
            
            if(category == null) Log.i(TAG, "readNonexistentCategoryFromDb_returnsNull: null")
            else Log.i(TAG, "readNonexistentCategoryFromDb_returnsNull: element - name: ${category.name}")

            assertNull(category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask() {
        runBlocking {
            val categoriesWithTasks = dao.getAllCategoriesWithTasks().first()

            Log.i(TAG, "readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask: rows - ${categoriesWithTasks.size}")
            Log.i(TAG, "readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask: associated tasks in first category - ${categoriesWithTasks[0].tasks.size}")
            Log.i(TAG, "readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask: first category - name: ${categoriesWithTasks[0].category.name}")
            Log.i(TAG, "readAllCategoriesWithTasks_returnsTwoRows_firstCategoryWithOneTask: first task - name: ${categoriesWithTasks[0].tasks[0].name}")
            
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
            
            Log.i(TAG, "readSecondCategoryWithTasks_categoryWithFourTasks_firstAndLastTaskCorrect: associated tasks in first category - ${categoryWithTasks?.tasks?.size}")
            Log.i(TAG, "readSecondCategoryWithTasks_categoryWithFourTasks_firstAndLastTaskCorrect: first category - name: ${categoryWithTasks?.category?.name}")
            Log.i(TAG, "readSecondCategoryWithTasks_categoryWithFourTasks_firstAndLastTaskCorrect: first task - name: ${categoryWithTasks?.tasks?.first()?.name}")
            Log.i(TAG, "readSecondCategoryWithTasks_categoryWithFourTasks_firstAndLastTaskCorrect: last task - name: ${categoryWithTasks?.tasks?.last()?.name}")
            
            assertTrue(categoryWithTasks?.tasks?.size == 4)
            assertEquals(TestData.CategoryData.categories[1], categoryWithTasks?.category)
            assertEquals(TestData.TaskData.tasks[1], categoryWithTasks?.tasks?.first())
            assertEquals(TestData.TaskData.tasks[4], categoryWithTasks?.tasks?.last())
        }
    }

    @Test
    @Throws(Exception::class)
    fun readNonexistentCategoryWithTasks_returnsNull() {
        runBlocking {
            val categoryWithTasks = dao.getCategoryWithTasksById(3).first()

            if(categoryWithTasks == null) Log.i(TAG, "readNonexistentCategoryWithTasks_returnsNull: is null")
            else {
                Log.i(TAG, "readNonexistentCategoryWithTasks_returnsNull: category - name: ${categoryWithTasks.category.name}")
                Log.i(TAG, "readNonexistentCategoryWithTasks_returnsNull: category - tasks: ${categoryWithTasks.tasks.size}")
            }

            assertNull(categoryWithTasks)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertNewCategory_lastElementAsNewCategory() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "insertNewCategory_lastElementAsNewCategory: size pre insert - ${categories.size}")
            
            dao.insertCategory(TestData.CategoryData.category_3_insert)

            categories = dao.getAllCategories().first()

            Log.i(TAG, "insertNewCategory_lastElementAsNewCategory: size post insert - ${categories.size}")
            Log.i(TAG, "insertNewCategory_lastElementAsNewCategory: inserted category - ${categories.last().name}")

            assertEquals(TestData.CategoryData.category_3_insert.copy(id = 3), categories.last())
            assertTrue(categories.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun insertDuplicateCategory_lastElementAsFirstInserted() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "insertDuplicateCategory_lastElementAsFirstInserted: size pre insert - ${categories.size}")

            dao.insertCategory(TestData.CategoryData.category_3_insert)
            dao.insertCategory(TestData.CategoryData.category_3_duplicate)

            categories = dao.getAllCategories().first()

            Log.i(TAG, "insertDuplicateCategory_lastElementAsFirstInserted: size post insert - ${categories.size}")
            Log.i(TAG, "insertDuplicateCategory_lastElementAsFirstInserted: inserted category - ${categories.last().name}")

            assertEquals(TestData.CategoryData.category_3_insert.copy(id = 3), categories.last())
            assertTrue(categories.size == 3)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateSecondCategory_updateSuccessful() {
        runBlocking {
            var category = dao.getCategoryById(2).first()

            Log.i(TAG, "updateSecondCategory_updateSuccessful: pre update - name: ${category?.name}")

            dao.updateCategory(TestData.CategoryData.category_2_update)

            category = dao.getCategoryById(2).first()

            Log.i(TAG, "updateSecondCategory_updateSuccessful: post update - name: ${category?.name}")

            assertEquals(TestData.CategoryData.category_2_update, category)
        }
    }

    @Test
    @Throws(Exception::class)
    fun updateNonexistentCategory_nothingHappens() {
        runBlocking {
            dao.updateCategory(TestData.CategoryData.category_3_insert)

            val categories = dao.getAllCategories().first()

            Log.i(TAG, "updateNonexistentCategory_nothingHappens: rows - ${categories.size}")
            Log.i(TAG, "updateNonexistentCategory_nothingHappens: firstElement - name: ${categories[0].name}")
            Log.i(TAG, "updateNonexistentCategory_nothingHappens: lastElement - name: ${categories[1].name}")


            assertTrue(categories.size == 2)
            assertEquals(TestData.CategoryData.categories[0], categories[0])
            assertEquals(TestData.CategoryData.categories[1], categories[1])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteFirstCategory_returnsOnlySecondCategory() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteFirstCategory_returnsOnlySecondCategory: pre delete - size: ${categories.size}")

            dao.deleteCategory(TestData.CategoryData.categories[0])

            categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteFirstCategory_returnsOnlySecondCategory: post delete - size: ${categories.size}")
            Log.i(TAG, "deleteFirstCategory_returnsOnlySecondCategory: firstElement - name: ${categories[0].name}")

            assertTrue(categories.size == 1)
            assertEquals(TestData.CategoryData.categories[1], categories[0])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteNonexistentCategory_nothingHappens() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteNonexistentCategory_nothingHappens: pre delete - size: ${categories.size}")

            dao.deleteCategory(TestData.CategoryData.category_3_insert)

            categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteNonexistentCategory_nothingHappens: post delete - size: ${categories.size}")

            assertTrue(categories.size == 2)
            assertEquals(TestData.CategoryData.categories[0], categories[0])
            assertEquals(TestData.CategoryData.categories[1], categories[1])
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteAllCategories_returnsEmptyList() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteAllCategories_returnsEmptyList: pre delete - size: ${categories.size}")

            dao.deleteCategories(TestData.CategoryData.categories[0], TestData.CategoryData.categories[1])

            categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteAllCategories_returnsEmptyList: post delete - size: ${categories.size}")

            assertTrue(categories.isEmpty())
        }
    }

    @Test
    @Throws(Exception::class)
    fun deleteMultipleCategories_nonexistentAndFirstCategory_returnsListWithSecondCategory() {
        runBlocking {
            var categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteMultipleCategories_nonexistentAndFirstCategory_returnsListWithSecondCategory: pre delete - size: ${categories.size}")

            dao.deleteCategories(TestData.CategoryData.categories[0], TestData.CategoryData.category_3_insert)

            categories = dao.getAllCategories().first()

            Log.i(TAG, "deleteMultipleCategories_nonexistentAndFirstCategory_returnsListWithSecondCategory: post delete - size: ${categories.size}")

            assertTrue(categories.size == 1)
            assertEquals(TestData.CategoryData.categories[1], categories[0])
        }
    }
}