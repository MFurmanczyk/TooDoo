package com.mfurmanczyk.toodoo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

@Dao
interface TaskDao {

    @Query("SELECT * FROM tasks")
    fun getAllTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE category_id IS NULL")
    fun getAllUncategorizedTasks(): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskById(id: Long): Flow<Task?>

    @Query("SELECT * FROM tasks WHERE is_done = :isDone")
    fun getTasksByIsDone(isDone: Boolean): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE due_date < :date")
    fun getTasksBeforeDate(date: LocalDate): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE due_date > :date")
    fun getTasksAfterDate(date: LocalDate): Flow<List<Task>>

    @Query("SELECT * FROM tasks WHERE due_date = :date")
    fun getTasksByDate(date: LocalDate): Flow<List<Task>>

    @Transaction
    @Query("SELECT * FROM tasks")
    fun getAllTasksWithSteps(): Flow<List<TaskWithSteps>>

    @Transaction
    @Query("SELECT * FROM tasks WHERE id = :id")
    fun getTaskWithStepsById(id: Long): Flow<TaskWithSteps?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTask(task: Task)

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateTask(task: Task)

    @Delete
    suspend fun deleteTask(task: Task)

    @Delete
    suspend fun deleteTasks(vararg tasks: Task)

}