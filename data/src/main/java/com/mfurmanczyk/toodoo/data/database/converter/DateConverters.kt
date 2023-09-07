package com.mfurmanczyk.toodoo.data.database.converter

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Room Database converters for [LocalDate] type.
 */
class LocalDateConverter {

    /**
     * Converts [timestamp] of type [Long] to [LocalDate].
     */
    @TypeConverter
    fun fromTimestamp(timestamp: Long?) = timestamp?.let { LocalDate.ofEpochDay(it) }

    /**
     * Converts [date] of type [LocalDate] to timestamp of type [Long].
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDate?) = date?.toEpochDay()

}

/**
 * Room Database converters for [LocalDateTime] type.
 */
class LocalDateTimeConverter {

    /**
     * Converts [timestamp] of type [Long] to [LocalDateTime].
     */
    @TypeConverter
    fun fromTimestamp(timestamp: Long?) = timestamp?.let { LocalDateTime.ofEpochSecond(it, 0, ZoneOffset.UTC) }

    /**
     * Converts [date] of type [LocalDateTime] to timestamp of type [Long].
     */
    @TypeConverter
    fun dateToTimestamp(date: LocalDateTime?) = date?.toEpochSecond(ZoneOffset.UTC)
}