package com.mfurmanczyk.toodoo.mobile.viewmodel

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.TaskWithSteps
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.data.repository.StepRepository
import com.mfurmanczyk.toodoo.data.repository.TaskRepository
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.view.screen.TaskEntryDestination
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidTaskNameException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject


data class TaskEntryUiState(
    val taskId: Long = 0L,
    val newEntry: Boolean,

    //editable
    val taskName: String = "",
    val taskDescription: String? = null,
    val dueDate: LocalDate = LocalDate.now(),
    val categoryId: Long? = null,
    //END: editable

    val isDone: Boolean = false,
    val createdOn: LocalDateTime,
    val completedOn: LocalDateTime? = null,
    val selectedCategoryName: String? = null,

    val categoryList: List<Category> = listOf(),

    val isAddingNewStep: Boolean = false,
    val stepDescription: String? = null,
    private val _stepsList: MutableList<Step> = mutableListOf(),
) {
    val stepsList: List<Step> = _stepsList
}

private const val TAG = "TaskEntryViewModel"

@HiltViewModel
class TaskEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @RoomDataSource private val taskRepository: TaskRepository,
    @RoomDataSource private val stepRepository: StepRepository,
    @RoomDataSource private val categoryRepository: CategoryRepository
) : ViewModel() {

    private val taskId = savedStateHandle[TaskEntryDestination.parameterName] ?: 0L

    private val _uiState = MutableStateFlow(TaskEntryUiState(newEntry = true, createdOn = LocalDateTime.now()))
    val uiState = _uiState.asStateFlow()

    init {
        Log.w(TAG, "Hello world! $taskId")
        viewModelScope.launch {
            refreshUiState()
        }
    }

    private suspend fun refreshUiState() {
        val categoryList = categoryRepository.getAllCategories().first()
        _uiState.update {
            it.copy(
                categoryList = categoryList,
            )
        }
        try {
            val taskWithSteps = taskRepository.getTaskWithStepsById(taskId).filterNotNull().first()
            _uiState.update {
                it.copy(
                    taskId = taskId,
                    newEntry = false,
                    taskName = taskWithSteps.task.name,
                    taskDescription = taskWithSteps.task.description,
                    dueDate = taskWithSteps.task.dueDate,
                    categoryId = taskWithSteps.task.categoryId,
                    isDone = taskWithSteps.task.isDone,
                    createdOn = taskWithSteps.task.createdOn,
                    completedOn = taskWithSteps.task.completedOn,
                    selectedCategoryName = if (it.categoryId == null) null else categoryList.first { category -> category.id == it.categoryId }.name,
                    isAddingNewStep = it.isAddingNewStep,
                    stepDescription = it.stepDescription,
                    _stepsList = taskWithSteps.steps.toMutableList().intersect(it.stepsList.toSet()).toMutableList(),

                )
            }
        } catch (e: NoSuchElementException) {
            _uiState.update {
                it.copy(
                    taskId = taskId,
                    newEntry = true,
                    taskName = it.taskName,
                    taskDescription =it.taskDescription,
                    dueDate = it.dueDate,
                    categoryId = it.categoryId,
                    isDone = it.isDone,
                    createdOn = it.createdOn,
                    completedOn = it.completedOn,
                    selectedCategoryName = if (it.categoryId == null) null else categoryList.first { category -> category.id == it.categoryId }.name,
                    isAddingNewStep = it.isAddingNewStep,
                    stepDescription = it.stepDescription,
                    _stepsList = it.stepsList.toMutableList()
                )
            }
        }

    }

    fun switchStepEntryMode(isActive: Boolean) = _uiState.update {
        it.copy(
            isAddingNewStep = isActive
        )
    }

    fun updateStepDescription(description: String) = _uiState.update {
        it.copy(
            stepDescription = description
        )
    }

    fun updateTaskName(taskName: String) = _uiState.update {
        it.copy(
            taskName = taskName
        )
    }

    fun updateTaskDescription(taskDescription: String) = _uiState.update {
        it.copy(
            taskDescription = taskDescription
        )
    }

    fun updateSelectedCategory(category: Category) = _uiState.update {
        it.copy(
            categoryId = category.id,
            selectedCategoryName = category.name
        )
    }

    fun deleteStep(step: Step) {
        val newStepList = _uiState.value.stepsList.toMutableList()
        newStepList.remove(step)
        _uiState.update {
            it.copy(
                _stepsList = newStepList
            )
        }
        if(!_uiState.value.newEntry) {
            viewModelScope.launch {
                stepRepository.removeStep(step)
                refreshUiState()
            }
        }
    }

    fun addStep(stepDescription: String) {
        viewModelScope.launch {
            val newStepList = _uiState.value.stepsList.toMutableList()
            val step = Step (
                taskId = taskId,
                description = stepDescription,
                createdOn = LocalDateTime.now(),
                isDone = false
            )
            newStepList.add(step)
            _uiState.update {
                it.copy(
                    _stepsList = newStepList
                )
            }
        }

    }

    fun addNewTaskWithSteps() {
        viewModelScope.launch {
            val taskId = addNewTask()
            addStepsForTask(taskId)
        }
    }

    private suspend fun addStepsForTask(taskId: Long) {
        _uiState.value.stepsList.forEach {
            stepRepository.addStep(
                it.copy(
                    taskId = taskId,
                    createdOn = LocalDateTime.now()
                )
            )
        }
    }

    private suspend fun addNewTask(): Long {
        val taskToAdd = _uiState.value.toTaskWithSteps().task
        return taskRepository.addTask(taskToAdd)
    }
}

@Throws(InvalidTaskNameException::class)
fun TaskEntryUiState.toTaskWithSteps(): TaskWithSteps {
    if(isValid()) {
        return TaskWithSteps(
            task = Task(
                id = taskId,
                categoryId = categoryId,
                name = taskName,
                description = taskDescription,
                createdOn = createdOn,
                completedOn = completedOn,
                dueDate = dueDate,
                isDone = isDone
            ),
            steps = stepsList
        )
    } else throw InvalidTaskNameException(R.string.task_name_cannot_be_null)
}

fun TaskEntryUiState.isValid() =
    taskName.isNotBlank()