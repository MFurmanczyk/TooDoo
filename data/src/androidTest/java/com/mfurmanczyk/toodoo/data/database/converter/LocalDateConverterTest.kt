package com.mfurmanczyk.toodoo.data.database.converter

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertThrows
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import java.time.DateTimeException
import java.time.LocalDate

@RunWith(JUnit4::class)
class LocalDateConverterTest {

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsPositiveTimestamp_returnsCorrectDate() {

        val timestamp = 1293781142L

        val actual = LocalDateConverter().fromTimestamp(timestamp)
        val expected = LocalDate.ofEpochDay(timestamp)

        assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsNegativeTimestamp_returnsCorrectDate() {
        val timestamp = -1861980298L

        val actual = LocalDateConverter().fromTimestamp(timestamp)
        val expected = LocalDate.ofEpochDay(timestamp)

        assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsZero_returnsCorrectDate() {
        val timestamp = 0L

        val actual = LocalDateConverter().fromTimestamp(timestamp)
        val expected = LocalDate.ofEpochDay(timestamp)

        assertEquals(expected, actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsNull_returnsNull() {
        val timestamp : Long? = null
        val actual = LocalDateConverter().fromTimestamp(timestamp)

        assertNull(actual)
    }

    @Test
    @Throws(Exception::class)
    fun fromTimestamp_convertsMaxPlusOne_throwsException() {
        assertThrows(DateTimeException::class.java) {
            val timestamp = LocalDate.MAX.toEpochDay() + 1L

            LocalDateConverter().fromTimestamp(timestamp)

        }
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_positiveDate_returnsPositiveTimestamp() {
        val date = LocalDate.of(2011, 11, 11)

        val actual = LocalDateConverter().dateToTimestamp(date)
        val expected = date.toEpochDay()

        assertEquals(expected, actual)
        assertTrue(actual!! > 0L)
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_negativeDate_returnsNegetiveTimestamp() {
        val date = LocalDate.of(1900, 11, 11)

        val actual = LocalDateConverter().dateToTimestamp(date)
        val expected = date.toEpochDay()

        assertEquals(expected, actual)
        assertTrue(actual!! < 0L)
    }

    @Test
    @Throws(Exception::class)
    fun dateToTimestamp_nullDate_returnsNull() {
        val actual = LocalDateConverter().dateToTimestamp(null)

        assertNull(actual)
    }
}