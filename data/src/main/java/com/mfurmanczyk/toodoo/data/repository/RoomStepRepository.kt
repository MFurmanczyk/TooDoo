package com.mfurmanczyk.toodoo.data.repository

import com.mfurmanczyk.toodoo.data.database.dao.StepDao
import com.mfurmanczyk.toodoo.data.model.Step
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RoomStepRepository @Inject constructor(private val dao: StepDao): StepRepository {
    override fun getAllSteps(): Flow<List<Step>> = dao.getAllSteps()

    override fun getStepById(id: Long): Flow<Step?> = dao.getStepById(id)

    override suspend fun addStep(step: Step) = dao.insertStep(step)

    override suspend fun updateStep(step: Step) = dao.updateStep(step)

    override suspend fun removeStep(step: Step) = dao.deleteStep(step)

    override suspend fun removeSteps(vararg steps: Step) = dao.deleteSteps(steps = steps)
}