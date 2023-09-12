package com.mfurmanczyk.toodoo.mobile.screen

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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.viewmodel.WelcomeScreenUIState
import com.mfurmanczyk.toodoo.mobile.viewmodel.isValid
import com.mfurmanczyk.toodoo.ui.theme.TooDooTheme

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
            verticalArrangement = Arrangement.spacedBy(8.dp, Alignment.CenterVertically),
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

        val uiState = remember { WelcomeScreenUIState() }

        WelcomeScreen(
            uiState = uiState,
            onInputFieldValueChanged = {
                uiState.username?.replace(uiState.username, it)
            },
            onSaveClick = {/* EMPTY */}
        )
    }
}