package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.activity.ComponentActivity
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsFocused
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performImeAction
import androidx.compose.ui.test.performTextInput
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenUIState
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test

class WelcomeScreenTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_welcomeScreenQuestion_questionIsDisplayed() {
        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }
        rule.onNodeWithContentDescription("Welcome screen question")
            .assertIsDisplayed()
            .assert(hasText(rule.activity.getString(R.string.welcome_screen_question)))
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_usernameInputField_fieldIsDisplayedAndFocusedAfterClick() {
        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }

        rule.onNodeWithContentDescription("Username input field")
            .performClick()

        rule.onNodeWithContentDescription("Username input field")
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsFocused()
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_usernameInputField_fieldPopulatedWithInput() {
        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }

        val input = "TEST"

        rule.onNodeWithContentDescription("Username input field")
            .performClick()
            .performTextInput(input)

        rule.onNodeWithContentDescription("Username input field")
            .assert(hasText(input))
            .assertIsDisplayed()
            .assertIsEnabled()
            .assertIsFocused()
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_usernameNull_buttonNotEnabled() {
        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }

        rule.onNodeWithContentDescription("Button")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_usernameAsSpace_buttonNotEnabled() {

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }

        val blankString = " "

        rule.onNodeWithContentDescription("Username input field")
            .performTextInput(blankString)

        rule.onNodeWithContentDescription("Username input field")
            .assert(hasText(blankString))

        rule.onNodeWithContentDescription("Button")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_usernameCorrect_buttonEnabled() {

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {

                }
            )
        }

        val input = "TEST"

        rule.onNodeWithContentDescription("Username input field")
            .performTextInput(input)

        rule.onNodeWithContentDescription("Username input field")
            .assert(hasText(input))

        rule.onNodeWithContentDescription("Button")
            .assertIsDisplayed()
            .assertIsEnabled()
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_performClick_usernameNull_clickNotPerformed() {

        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        rule.onNodeWithContentDescription("Button").performClick()

        assertEquals(0, counterState)
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_performClick_usernameBlank_clickNotPerformed() {
        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        val input = " "
        rule.onNodeWithContentDescription("Username input field").performTextInput(input)
        rule.onNodeWithContentDescription("Username input field").assert(hasText(input))

        rule.onNodeWithContentDescription("Button").performClick()

        assertEquals(0, counterState)
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_saveButton_performClick_usernameCorrect_clickPerformed() {
        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        val input = "TEST"
        rule.onNodeWithContentDescription("Username input field").performTextInput(input)
        rule.onNodeWithContentDescription("Username input field").assert(hasText(input))

        rule.onNodeWithContentDescription("Button").performClick()

        assertEquals(1, counterState)
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_performImeAction_usernameNull_actionNotPerformed() {
        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        rule.onNodeWithContentDescription("Username input field").performImeAction()
        assertEquals(0, counterState)
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_performImeAction_usernameBlank_actionNotPerformed() {
        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        val input = " "
        rule.onNodeWithContentDescription("Username input field").performTextInput(input)
        rule.onNodeWithContentDescription("Username input field").assert(hasText(input))
        rule.onNodeWithContentDescription("Username input field").performImeAction()
        assertEquals(0, counterState)
    }

    @Test
    @Throws(Exception::class)
    fun welcomeScreen_performImeAction_usernameCorrect_actionPerformed() {
        var counterState by mutableIntStateOf(0)

        rule.setContent {

            val welcomeScreenUIState = remember { mutableStateOf(WelcomeScreenUIState()) }

            WelcomeScreen(
                uiState = welcomeScreenUIState.value,
                onInputFieldValueChanged = {
                    welcomeScreenUIState.value = WelcomeScreenUIState(it)
                },
                onSaveClick = {
                    counterState++
                }
            )
        }

        val input = "TEST"
        rule.onNodeWithContentDescription("Username input field").performTextInput(input)
        rule.onNodeWithContentDescription("Username input field").assert(hasText(input))
        rule.onNodeWithContentDescription("Username input field").performImeAction()
        assertEquals(1, counterState)
    }
}