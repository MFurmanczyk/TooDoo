package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.activity.ComponentActivity
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.Add
import androidx.compose.material.icons.twotone.Face
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import org.junit.Rule
import org.junit.Test


class ExpandableFloatingActionButtonTest {

    @get:Rule
    val rule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @Throws(Exception::class)
    fun actionButton_buttonIsDisplayed() {
        rule.setContent {

            TestFloatingActionButton()

        }

        rule.onNodeWithContentDescription(ContentDescriptors.ACTION_BUTTON).assertIsDisplayed()
    }

    @Test
    @Throws(Exception::class)
    fun actionButton_buttonClick_twoActionsDisplayed() {
        rule.setContent {

            TestFloatingActionButton()

        }

        rule.onNodeWithContentDescription(ContentDescriptors.ACTION_BUTTON).assertIsDisplayed().performClick()
        rule.onNodeWithContentDescription(ContentDescriptors.FIRST_ACTION).assertIsDisplayed()
        rule.onNodeWithContentDescription(ContentDescriptors.SECOND_ACTION).assertIsDisplayed()
    }

    @Test
    @Throws(Exception::class)
    fun actionButton_buttonClick_twoActionsDisplayed_firstActionClick_actionsDismissed() {
        rule.setContent {

            TestFloatingActionButton()

        }

        rule.onNodeWithContentDescription(ContentDescriptors.ACTION_BUTTON)
            .assertIsDisplayed()
            .performClick()
        rule.onNodeWithContentDescription(ContentDescriptors.SECOND_ACTION)
            .assertIsDisplayed()
        rule.onNodeWithContentDescription(ContentDescriptors.FIRST_ACTION)
            .assertIsDisplayed()
        rule.onNodeWithContentDescription(ContentDescriptors.FIRST_ACTION)
            .performClick()
            .assertDoesNotExist()
        rule.onNodeWithContentDescription(ContentDescriptors.SECOND_ACTION)
            .assertDoesNotExist()

    }
}


@Composable
fun TestFloatingActionButton() {
    val actionButtonState = rememberExpandableFloatingActionButtonState()

    ExpandableFloatingActionButton(
        state = actionButtonState,
        subActions = { visible ->

            ExpandableAction(
                modifier = Modifier.semantics { contentDescription = ContentDescriptors.FIRST_ACTION },
                actionVisible = visible,
                onClick = {
                    actionButtonState.collapse()
                },
                enter = slideInVertically {
                    2 * it
                },
                exit = scaleOut() + slideOutVertically {
                    2 * it
                }
            ) {
                Icon(imageVector = Icons.TwoTone.Face, contentDescription = null)
            }

            ExpandableAction(
                modifier = Modifier.semantics { contentDescription =  ContentDescriptors.SECOND_ACTION},
                actionVisible = visible,
                onClick = {
                    actionButtonState.collapse()
                },
                enter = slideInVertically {
                    1 * it
                },
                exit = scaleOut() + slideOutVertically {
                    1 * it
                }
            ) {
                Icon(imageVector = Icons.TwoTone.Face, contentDescription = null)
            }
        },
        modifier = Modifier.semantics { contentDescription =  ContentDescriptors.ACTION_BUTTON}
    ) {
        val rotation by animateFloatAsState(targetValue = if (it) 45f else 0f)
        Icon(
            imageVector = Icons.TwoTone.Add,
            contentDescription = null,
            modifier = Modifier.rotate(rotation)
        )
    }
}

object ContentDescriptors {
    val ACTION_BUTTON = "Action button"
    val FIRST_ACTION = "First action"
    val SECOND_ACTION = "Second action"
}
