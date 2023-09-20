package com.mfurmanczyk.toodoo.mobile.view.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowForward
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTag
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenUIState
import com.mfurmanczyk.toodoo.mobile.viewmodel.isValid

const val BUTTON_TEST_TAG = "save button"
@Composable
fun WelcomeScreen(
    uiState: WelcomeScreenUIState,
    onInputFieldValueChanged: (String) -> Unit,
    onSaveClick: () -> Unit,
    modifier: Modifier = Modifier
) {

    Surface(
        modifier = modifier.fillMaxSize()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.medium, Alignment.CenterVertically),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxSize()
        ) {
            Text(
                text = stringResource(R.string.welcome_screen_question),
                style = MaterialTheme.typography.headlineSmall
            )
            OutlinedTextField(
                value = uiState.username ?: "",
                onValueChange = onInputFieldValueChanged,
                label = {
                    Text(text = stringResource(R.string.name))
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Go
                ),
                keyboardActions = KeyboardActions(
                    onGo = {
                        onSaveClick()
                    }
                )
            )
            OutlinedIconButton(
                modifier = Modifier
                    .semantics {
                        testTag = BUTTON_TEST_TAG
                    },
                enabled = uiState.isValid(),
                onClick = onSaveClick
            ) {
                Icon(
                    imageVector = Icons.TwoTone.ArrowForward,
                    contentDescription = null
                )
            }
        }
    }
}

@Preview
@Composable
fun WelcomeScreenPreview() {
    TooDooTheme {

        val uiState = remember { mutableStateOf(WelcomeScreenUIState()) }

        WelcomeScreen(
            uiState = uiState.value,
            onInputFieldValueChanged = {
                uiState.value = WelcomeScreenUIState(it)
            },
            onSaveClick = {/* EMPTY */}
        )
    }
}