package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.model.relationship.CategoryWithTasks
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.data.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class DashboardScreenUIState(
    val categoryList: List<CategoryWithTasks>,
    val tasks: List<Task>
)

@HiltViewModel
class DashboardScreenViewModel @Inject constructor(
    @RoomDataSource val categoryRepository: CategoryRepository,
    @RoomDataSource val taskRepository: TaskRepository
) : ViewModel() {

    val uiState = categoryRepository.getAllCategoriesWithTasks().combine(taskRepository.getAllTasks()) { categories, tasks ->
        val todayTasks = tasks.filter {
            it.dueDate == LocalDate.now()
        }
        DashboardScreenUIState(categories, todayTasks)
    }.stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
        DashboardScreenUIState(listOf(), listOf())
    )

    fun updateTask(task: Task) {
        viewModelScope.launch {
            taskRepository.updateTask(task)
        }
    }

    companion object {
        val TIMEOUT_MILLIS = 5000L
    }

}

