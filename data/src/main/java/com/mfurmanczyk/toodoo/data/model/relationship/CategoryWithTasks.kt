package com.mfurmanczyk.toodoo.data.model.relationship

import androidx.room.Embedded
import androidx.room.Relation
import com.mfurmanczyk.toodoo.data.model.Category
import com.mfurmanczyk.toodoo.data.model.Task

data class CategoryWithTasks(
    @Embedded val category: Category,
    @Relation(
        parentColumn = "id",
        entityColumn = "category_id"
    )
    val tasks: List<Task>
)
