package com.mfurmanczyk.toodoo.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.mfurmanczyk.toodoo.data.database.converter.ColorConverter
import com.mfurmanczyk.toodoo.data.database.converter.LocalDateConverter
import com.mfurmanczyk.toodoo.data.database.converter.LocalDateTimeConverter
import com.mfurmanczyk.toodoo.data.database.dao.CategoryDao
import com.mfurmanczyk.toodoo.data.database.dao.StepDao
import com.mfurmanczyk.toodoo.data.database.dao.TaskDao
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task

@Database(entities = [Task::class, Category::class, Step::class], version = 1, exportSchema = false)
@TypeConverters(value = [LocalDateConverter::class, LocalDateTimeConverter::class, ColorConverter::class])
abstract class TooDooDatabase: RoomDatabase() {

    abstract fun taskDao(): TaskDao
    abstract fun stepDao(): StepDao
    abstract fun categoryDao(): CategoryDao
}