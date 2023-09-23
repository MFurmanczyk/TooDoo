package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.model.Step
import kotlinx.coroutines.flow.Flow

interface StepRepository {

    /**
     * Returns all steps from datasource.
     */
    fun getAllSteps(): Flow<List<Step>>

    /**
     * Returns [Step] with given [id] from datasource.
     */
    fun getStepById(id: Long): Flow<Step?>

    /**
     * Adds [step] to datasource.
     */
    suspend fun addStep(step: Step) : Long

    /**
     * Updates [step] in datasource.
     */
    suspend fun updateStep(step: Step)

    /**
     * Removes [step] from datasource.
     */
    suspend fun removeStep(step: Step)

    /**
     * Removes multiple [steps] from datasource.
     */
    suspend fun removeSteps(vararg steps: Step)
}