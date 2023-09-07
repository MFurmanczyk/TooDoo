package com.mfurmanczyk.toodoo.data.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset


class LocalDateConverter {

    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { LocalDate.ofEpochDay(it) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?) = date?.toEpochDay()

}

class LocalDateTimeConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?) = value?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }

    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?) = date?.toEpochSecond(ZoneOffset.UTC)
}