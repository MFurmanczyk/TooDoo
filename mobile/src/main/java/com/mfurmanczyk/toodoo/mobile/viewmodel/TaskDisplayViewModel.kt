package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Step
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.data.repository.StepRepository
import com.mfurmanczyk.toodoo.data.repository.TaskRepository
import com.mfurmanczyk.toodoo.mobile.view.screen.TaskDisplayDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import javax.inject.Inject

data class TaskDisplayUiState(
    val task: Task,
    val category: Category,
    val steps: List<Step>
)

data class TaskDisplayDialogState(
    val shouldDisplayDialog: Boolean
)

@HiltViewModel
class TaskDisplayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @RoomDataSource private val taskRepository: TaskRepository,
    @RoomDataSource private val categoryRepository: CategoryRepository,
    @RoomDataSource private val stepRepository: StepRepository
) : ViewModel() {

    private val taskId = savedStateHandle[TaskDisplayDestination.parameterName] ?: 0L

    val uiState = taskRepository.getTaskWithStepsById(taskId).filterNotNull().combine(categoryRepository.getAllCategories()) { task, categories ->
        TaskDisplayUiState(
            task = task.task,
            category = categories.firstOrNull() { it.id == task.task.categoryId } ?: Category.uncategorizedCategory(),
            steps = task.steps
        )
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000L),
        TaskDisplayUiState(
            task = Task(
                name = "Task not found",
                description = "Task not found",
                createdOn = LocalDateTime.now(),
                dueDate = LocalDate.now(),
                isDone = false
            ),
            category = Category.uncategorizedCategory(),
            steps = listOf()
        )
    )

    private val _dialogState = MutableStateFlow(TaskDisplayDialogState(false))
    val dialogState = _dialogState.asStateFlow()

    fun displayDialog() {
        _dialogState.update {
            it.copy(shouldDisplayDialog = true)
        }
    }

    fun hideDialog() {
        _dialogState.update {
            it.copy(shouldDisplayDialog = false)
        }
    }

    fun deleteTask() {
        viewModelScope.launch {
            taskRepository.removeTask(uiState.value.task)
        }
    }

    fun checkTask(checked: Boolean) {
        viewModelScope.launch {
            //task unchecked
            if (uiState.value.task.isDone && !checked) {
                taskRepository.updateTask(uiState.value.task.copy(isDone = false, completedOn = null))
            } else {
                //task checked
                if (checked) {
                    taskRepository.updateTask(
                        uiState.value.task.copy(
                            isDone = true,
                            completedOn = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }

    fun checkStep(step: Step, checked: Boolean) {
        viewModelScope.launch {
            if(step.isDone && !checked) {
                stepRepository.updateStep(step.copy(isDone = false))
            } else {
                if(checked) {
                    stepRepository.updateStep(step.copy(isDone = true))
                }
            }
        }
    }

}