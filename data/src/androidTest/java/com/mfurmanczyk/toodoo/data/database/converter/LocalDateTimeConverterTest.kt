package com.mfurmanczyk.toodoo.data.database.converter

import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.DateTimeException
import java.time.LocalDateTime
import java.time.ZoneOffset

@RunWith(JUnit4::class)
class LocalDateTimeConverterTest {

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsPositiveTimestamp_returnsCorrectDate() {

        val timestamp = 1293781142L

        val actual = LocalDateTimeConverter().fromTimestamp(timestamp)
        val expected = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)

        Assert.assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsNegativeTimestamp_returnsCorrectDate() {
        val timestamp = -1861980298L

        val actual = LocalDateTimeConverter().fromTimestamp(timestamp)
        val expected = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)

        Assert.assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsZero_returnsCorrectDate() {
        val timestamp = 0L

        val actual = LocalDateTimeConverter().fromTimestamp(timestamp)
        val expected = LocalDateTime.ofEpochSecond(timestamp, 0, ZoneOffset.UTC)

        Assert.assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsNull_returnsNull() {
        val actual = LocalDateTimeConverter().fromTimestamp(null)

        Assert.assertNull(actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsMaxPlusOne_throwsException() {
        Assert.assertThrows(DateTimeException::class.java) {
            val timestamp = LocalDateTime.MAX.toEpochSecond(ZoneOffset.UTC) + 1L

            LocalDateTimeConverter().fromTimestamp(timestamp)

        }
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_positiveDate_returnsPositiveTimestamp() {
        val date = LocalDateTime.of(2011, 11, 11, 0, 0)

        val actual = LocalDateTimeConverter().dateToTimestamp(date)
        val expected = date.toEpochSecond(ZoneOffset.UTC)

        Assert.assertEquals(expected, actual)
        Assert.assertTrue(actual!! > 0L)
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_negativeDate_returnsNegetiveTimestamp() {
        val date = LocalDateTime.of(1900, 11, 11,0 ,0)

        val actual = LocalDateTimeConverter().dateToTimestamp(date)
        val expected = date.toEpochSecond(ZoneOffset.UTC)

        Assert.assertEquals(expected, actual)
        Assert.assertTrue(actual!! < 0L)
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_nullDate_returnsNull() {
        val actual = LocalDateTimeConverter().dateToTimestamp(null)

        Assert.assertNull(actual)
    }
}