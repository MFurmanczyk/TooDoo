package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.CategoryDao
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomCategoryRepository @Inject constructor(private val dao: CategoryDao): CategoryRepository {
    override fun getAllCategories(): Flow<List<Category>> = dao.getAllCategories()

    override fun getCategoryById(id: Long): Flow<Category?> = dao.getCategoryById(id)

    override fun getAllCategoriesWithTasks(): Flow<List<CategoryWithTasks>> = dao.getAllCategoriesWithTasks()

    override fun getCategoryWithTask(category: Category): Flow<CategoryWithTasks?> = dao.getCategoryWithTasksById(category.id)

    override suspend fun addCategory(category: Category) = dao.insertCategory(category)

    override suspend fun updateCategory(category: Category) = dao.updateCategory(category)

    override suspend fun removeCategory(category: Category) = dao.deleteCategory(category)

    override suspend fun removeCategories(vararg categories: Category) = dao.deleteCategories(categories = categories)
}