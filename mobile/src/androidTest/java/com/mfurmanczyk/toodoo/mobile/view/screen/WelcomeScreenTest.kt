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
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isNotFocusable
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
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
        rule.onNodeWithText(rule.activity.getString(R.string.welcome_screen_question))
            .assertIsDisplayed()
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

        rule.onNode(hasText(rule.activity.getString(R.string.user_name)))
            .performClick()

        rule.onNode(hasText(rule.activity.getString(R.string.user_name)))
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

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
            .performTextInput(input)

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
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

        rule.onNodeWithTag(BUTTON_TEST_TAG)
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

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
            .performTextInput(blankString)

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
            .assert(hasText(blankString))

        rule.onNodeWithTag(BUTTON_TEST_TAG)
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

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
            .performTextInput(input)

        rule.onNodeWithText(rule.activity.getString(R.string.user_name))
            .assert(hasText(input))

        rule.onNodeWithTag(BUTTON_TEST_TAG)
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

        rule.onNode(hasClickAction() and hasTestTag("save button")).performClick()

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
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performTextInput(input)
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).assert(hasText(input))

        rule.onNode(hasClickAction() and isNotFocusable()).performClick()

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
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performTextInput(input)
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).assert(hasText(input))

        rule.onNode(hasClickAction() and hasTestTag("save button")).performClick()

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

        rule.onNode(hasText(rule.activity.getString(R.string.user_name))).performImeAction()
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
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performTextInput(input)
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).assert(hasText(input))
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performImeAction()
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
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performTextInput(input)
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).assert(hasText(input))
        rule.onNodeWithText(rule.activity.getString(R.string.user_name)).performImeAction()
        assertEquals(1, counterState)
    }
}