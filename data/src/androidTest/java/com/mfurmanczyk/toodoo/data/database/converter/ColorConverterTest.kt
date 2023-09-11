package com.mfurmanczyk.toodoo.data.database.converter

import android.graphics.Color
import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ColorConverterTest {

    @Test
    @Throws(Exception::class)
    fun colorToInteger_convertsBlueToInteger() {

            val color = Color.valueOf(Color.BLUE)
            val colorInteger = ColorConverter().colorToInteger(color)

            assertEquals(Color.BLUE, colorInteger)
    }

    @Test
    @Throws(Exception::class)
    fun colorToInteger_convertsNull() {
        val colorInteger = ColorConverter().colorToInteger(null)

        assertNull(colorInteger)
    }

    @Test
    @Throws(Exception::class)
    fun integerToColor_convertsIntegerToBlueColor() {
        val integer = Color.BLUE
        val color = ColorConverter().integerToColor(integer)

        assertEquals(Color.valueOf(Color.BLUE), color)
    }

    @Test
    @Throws(Exception::class)
    fun integerToColor_convertsNull() {
        val color = ColorConverter().integerToColor(null)

        assertNull(color)
    }
}