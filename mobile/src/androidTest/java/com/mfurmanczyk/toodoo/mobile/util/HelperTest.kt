package com.mfurmanczyk.toodoo.mobile.util

import androidx.compose.ui.graphics.Color
import com.mfurmanczyk.toodoo.data.model.ColorHolder
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class HelperTest {

    @Test
    @Throws(Exception::class)
    fun colorHolderToComposeColorTest_redColor() {
        val colorHolder = ColorHolder(alpha = 255, red = 255, green = 0, blue = 0)
        val composeColor = Color.Red

        val actual = colorHolder.toComposeColor()

        assertEquals(composeColor, actual)
    }

    @Test
    @Throws(Exception::class)
    fun composeColorToColorHolder_greenColor() {
        val composeColor = Color.Green
        val colorHolder = ColorHolder(255, 0, 255, 0)

        val actual = composeColor.toColorHolder()

        assertEquals(colorHolder, actual)
    }

    @Test
    @Throws(Exception::class)
    fun composeColorToColorHolder_lightGreyColor() {
        val composeColor = Color.LightGray
        val colorHolder = ColorHolder(255, 204, 204, 204)

        val actual = composeColor.toColorHolder()

        assertEquals(colorHolder, actual)
    }

    @Test
    @Throws(Exception::class)
    fun composeColorToColorHolder_darkGreyColor() {
        val composeColor = Color.DarkGray
        val colorHolder = ColorHolder(255, 68, 68, 68)

        val actual = composeColor.toColorHolder()

        assertEquals(colorHolder, actual)
    }

    @Test
    @Throws(Exception::class)
    fun composeColorToColorHolder_onlyOnes() {
        val composeColor = Color(1,1 ,1,1)
        val colorHolder = ColorHolder(1, 1, 1, 1)

        val actual = composeColor.toColorHolder()

        assertEquals(colorHolder, actual)
    }

}