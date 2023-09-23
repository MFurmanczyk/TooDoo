package com.mfurmanczyk.toodoo.data.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.mfurmanczyk.toodoo.data.model.Step
import kotlinx.coroutines.flow.Flow

@Dao
interface StepDao {

    @Query("SELECT * FROM steps")
    fun getAllSteps(): Flow<List<Step>>

    @Query("SELECT * FROM steps WHERE id = :id")
    fun getStepById(id: Long): Flow<Step?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertStep(step: Step) : Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun updateStep(step: Step)

    @Delete
    suspend fun deleteStep(step: Step)

    @Delete
    suspend fun deleteSteps(vararg steps: Step)

}