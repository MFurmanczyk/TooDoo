package com.mfurmanczyk.toodoo.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.ForeignKey.Companion.CASCADE
import androidx.room.PrimaryKey
import java.time.LocalDateTime

@Entity(
    foreignKeys = [ForeignKey(
        entity = Task::class,
        parentColumns = ["id"],
        childColumns = ["taskId"],
        onDelete = CASCADE
    )]
)
data class Step(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val taskId: Long = 0,
    val description: String,
    @ColumnInfo(name = "created_on") val createdOn: LocalDateTime,
    @ColumnInfo(name = "is_done") val isDone: Boolean
)