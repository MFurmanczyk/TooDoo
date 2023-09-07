package com.mfurmanczyk.toodoo.data.di

import android.content.Context
import androidx.room.Room
import com.mfurmanczyk.toodoo.data.database.TooDooDatabase
import com.mfurmanczyk.toodoo.data.database.dao.CategoryDao
import com.mfurmanczyk.toodoo.data.database.dao.StepDao
import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.data.repository.RoomCategoryRepository
import com.mfurmanczyk.toodoo.data.repository.RoomStepRepository
import com.mfurmanczyk.toodoo.data.repository.RoomTaskRepository
import com.mfurmanczyk.toodoo.data.repository.StepRepository
import com.mfurmanczyk.toodoo.data.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Dependency Injection Hilt [Module] that provides local database dependencies.
 */

@InstallIn(SingletonComponent::class)
@Module
class RoomDataModule {

    @Provides
    fun provideTaskDao(database: TooDooDatabase) = database.taskDao()

    @Provides
    fun provideStepDao(database: TooDooDatabase) = database.stepDao()

    @Provides
    fun provideCategoryDao(database: TooDooDatabase) = database.categoryDao()

    @Provides
    @RoomDataSource
    fun provideRoomCategoryRepository(dao: CategoryDao) : CategoryRepository = RoomCategoryRepository(dao)

    @Provides
    @RoomDataSource
    fun provideRoomTaskRepository(dao: TaskDao): TaskRepository = RoomTaskRepository(dao)

    @Provides
    @RoomDataSource
    fun provideRoomStepRepository(dao: StepDao): StepRepository = RoomStepRepository(dao)

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context) : TooDooDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = TooDooDatabase::class.java,
            "TooDoo"
        ).build()
    }
}