package com.mfurmanczyk.toodoo.mobile.view.component

import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun TooDooFab(
    onFirstActionClick: () -> Unit,
    onSecondActionClick: () -> Unit,
    firstActionContent: @Composable () -> Unit,
    secondActionContent: @Composable () -> Unit,
    modifier: Modifier = Modifier,
    state: ExpandableFloatingActionButtonState = rememberExpandableFloatingActionButtonState(),
    content: @Composable () -> Unit
) {
    ExpandableFloatingActionButton(
        modifier = modifier,
        state = state,
        subActions = { visible ->

            ExpandableAction(
                actionVisible = visible,
                onClick = {
                    state.collapse()
                    onFirstActionClick()
                },
                enter = slideInVertically {
                    2 * it
                },
                exit = scaleOut() + slideOutVertically {
                    2 * it
                }
            ) {
                firstActionContent()
            }

            ExpandableAction(
                actionVisible = visible,
                onClick = {
                    state.collapse()
                    onSecondActionClick()
                },
                enter = slideInVertically {
                    1 * it
                },
                exit = scaleOut() + slideOutVertically {
                    1 * it
                }
            ) {
                secondActionContent()
            }
        }
    ) {
        content()
    }
}