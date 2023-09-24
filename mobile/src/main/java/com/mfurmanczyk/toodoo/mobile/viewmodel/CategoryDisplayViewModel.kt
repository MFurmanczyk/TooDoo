package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Task
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.data.repository.TaskRepository
import com.mfurmanczyk.toodoo.mobile.util.toColorHolder
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoryDisplayDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import javax.inject.Inject

data class CategoryDisplayUiState(
    val category: Category,
    val taskList: List<Task>
)

@HiltViewModel
class CategoryDisplayViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @RoomDataSource private val categoryRepository: CategoryRepository,
    @RoomDataSource private val taskRepository: TaskRepository
) : DialogViewModel() {

    private val categoryId: Long = savedStateHandle[CategoryDisplayDestination.parameterName] ?: 0

    val uiState = (
            if (categoryId == 0L)
                taskRepository.getAllUncategorizedTasks().map {
                    CategoryDisplayUiState(
                        category = Category.uncategorizedCategory(),
                        taskList = it
                    )
                } else
                categoryRepository.getCategoryWithTaskById(categoryId).filterNotNull().map {
                    CategoryDisplayUiState(
                        category = it.category,
                        taskList = it.tasks
                    )
                }).stateIn(
                        viewModelScope,
                        SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                        CategoryDisplayUiState(
                            Category(name = "", color = Color.Gray.toColorHolder()),
                            listOf()
                        )
                )


    fun deleteCategory() {
        viewModelScope.launch {
            categoryRepository.removeCategory(uiState.value.category)
        }
    }

    fun checkTask(task: Task, checked: Boolean) {
        viewModelScope.launch {
            if (task.isDone) {
                //task unchecked
                if (!checked) {
                    taskRepository.updateTask(task.copy(isDone = false, completedOn = null))
                }
            } else {
                //task checked
                if (checked) {
                    taskRepository.updateTask(
                        task.copy(
                            isDone = true,
                            completedOn = LocalDateTime.now()
                        )
                    )
                }
            }
        }
    }

    companion object {
        const val TIMEOUT_MILLIS = 5000L
    }
}