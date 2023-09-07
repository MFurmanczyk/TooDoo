package com.mfurmanczyk.toodoo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.SET_DEFAULT
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Category::class,
        parentColumns = ["id"],
        childColumns = ["categoryId"],
        onDelete = SET_DEFAULT
    )]
)
data class Task(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val categoryId: Long = 0,
    val name: String,
    val description: String?,
    @ColumnInfo(name = "created_on") val createdOn: LocalDateTime,
    @ColumnInfo(name = "completed_on") val completedOn: LocalDateTime,
    @ColumnInfo(name = "due_date") val dueDate: LocalDate,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)
