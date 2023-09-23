package com.mfurmanczyk.toodoo.data.model

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @Embedded
    val color: ColorHolder
) {
    companion object {
        val Uncategorized = Category(
            name = "Uncategorized",
            color = ColorHolder(255, 88, 88, 88)
        )
    }
}
