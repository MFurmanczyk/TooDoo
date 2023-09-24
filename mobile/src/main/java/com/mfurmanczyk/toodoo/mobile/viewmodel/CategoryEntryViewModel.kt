package com.mfurmanczyk.toodoo.mobile.viewmodel

import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mfurmanczyk.toodoo.data.di.annotation.RoomDataSource
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.ColorHolder
import com.mfurmanczyk.toodoo.data.repository.CategoryRepository
import com.mfurmanczyk.toodoo.mobile.util.toColorHolder
import com.mfurmanczyk.toodoo.mobile.view.screen.CategoryEntryDestination
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidCategoryNameException
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryEntryScreenUiState(
    val categoryName: String? = null,
    val colorHolder: ColorHolder,
    val newEntry: Boolean,
)

@HiltViewModel
class CategoryEntryViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    @RoomDataSource private val categoryRepository: CategoryRepository
) : DialogViewModel() {

    private val categoryId: Long = savedStateHandle[CategoryEntryDestination.parameterName] ?: 0L

    private val _uiState =
        MutableStateFlow(
            CategoryEntryScreenUiState(
                categoryName = null,
                colorHolder = Color.Blue.toColorHolder(),
                newEntry = true
            )
        )
    val uiState = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val category = categoryRepository
                .getCategoryById(categoryId).filterNotNull().first()

            _uiState.update {
                it.copy(
                    categoryName = if (categoryId == 0L) null else category.name,
                    colorHolder = if (categoryId == 0L) Color.Blue.toColorHolder() else category.color,
                    newEntry = categoryId == 0L
                )
            }
        }
    }

    fun updateCategoryName(newName: String) {
        _uiState.update {
            it.copy(
                categoryName = newName
            )
        }
    }

    fun updateCategoryColor(newColor: Color) {
        _uiState.update {
            it.copy(
                colorHolder = newColor.toColorHolder()
            )
        }
    }

    @Throws(InvalidCategoryNameException::class)
    fun saveCategory() {
        if (uiState.value.isValid()) {
            viewModelScope.launch {
                if(_uiState.value.newEntry) {
                    categoryRepository.addCategory(uiState.value.toCategory())
                } else {
                    categoryRepository.updateCategory(uiState.value.toCategory(categoryId))
                }
            }
        } else throw InvalidCategoryNameException()
    }

}

fun CategoryEntryScreenUiState.isValid(): Boolean {
    return !categoryName.isNullOrBlank()
}

@Throws(InvalidCategoryNameException::class)
fun CategoryEntryScreenUiState.toCategory(id: Long = 0) : Category {
    if (categoryName == null) throw InvalidCategoryNameException()
    return Category(
        id = id,
        name = categoryName,
        color = colorHolder
    )
}