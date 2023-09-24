package com.mfurmanczyk.toodoo.data.model

import android.content.Context
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.mfurmanczyk.toodoo.data.R

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    @Embedded
    val color: ColorHolder
) {
    companion object {
        fun uncategorizedCategory(context: Context? = null) = Category(
            name = context?.getString(R.string.uncategorized) ?: "",
            color = ColorHolder(255, 88, 88, 88)
        )
    }
}
