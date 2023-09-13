package com.mfurmanczyk.toodoo.mobile.view.component


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SmallFloatingActionButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing


@Composable
fun ExpandableFloatingActionButton(
    modifier: Modifier = Modifier,
    state: ExpandableFloatingActionButtonState = rememberExpandableFloatingActionButtonState(),
    subActions: @Composable (ColumnScope.(Boolean) -> Unit),
    content: @Composable (Boolean)->Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
    ) {

        Column(
                modifier = Modifier.padding(vertical = MaterialTheme.spacing.small),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                subActions(this, state.isExpanded())
            }
        
        FloatingActionButton(
            onClick = {
                if(state.isExpanded()) state.collapse()
                else state.expand()
            }
        ) {
            content(state.isExpanded())
        }
    }
}

@Composable
fun ExpandableAction(
    actionVisible: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enter: EnterTransition = scaleIn(),
    exit: ExitTransition = scaleOut(),
    content: @Composable () -> Unit
) {
    AnimatedVisibility(
        visible = actionVisible,
        enter = enter,
        exit = exit,
        modifier = modifier
    ) {
        SmallFloatingActionButton(
            modifier = Modifier.focusable(true),
            onClick = onClick,
            content = content
        )
    }
}