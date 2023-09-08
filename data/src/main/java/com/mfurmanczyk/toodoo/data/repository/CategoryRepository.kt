package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {

    /**
     * Returns all categories from datasource.
     */
    fun getAllCategories(): Flow<List<Category>>

    /**
     * Returns category with given [id]
     */
    fun getCategoryById(id: Long): Flow<Category?>

    /**
     * Returns all categories with associated [Task]s wrapped in [CategoryWithTasks] class.
     */
    fun getAllCategoriesWithTasks(): Flow<List<CategoryWithTasks>>

    /**
     * Returns all associated [Task]s for given [category]. Wrapped in [CategoryWithTasks] class.
     */
    fun getCategoryWithTask(category: Category): Flow<CategoryWithTasks?>

    /**
     * Adds [category] to datasource.
     */
    suspend fun addCategory(category: Category)

    /**
     * Updates [category] in datasource
     */
    suspend fun updateCategory(category: Category)

    /**
     * Removes [category] from datasource.
     */
    suspend fun removeCategory(category: Category)

    /**
     * Removes multiple [categories] from datasource
     */
    suspend fun removeCategories(vararg categories: Category)
}