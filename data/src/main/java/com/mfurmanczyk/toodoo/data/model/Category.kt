package com.mfurmanczyk.toodoo.data.model

import android.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Category(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val color: Color
)
