package com.mfurmanczyk.toodoo.data.database.converter

import android.graphics.Color
import androidx.room.TypeConverter

class ColorConverter {

    @TypeConverter
    fun colorToInteger(color: Color?) = color?.toArgb()

    @TypeConverter
    fun integerToColor(value: Int?) = value?.let { Color.valueOf(it) }

}