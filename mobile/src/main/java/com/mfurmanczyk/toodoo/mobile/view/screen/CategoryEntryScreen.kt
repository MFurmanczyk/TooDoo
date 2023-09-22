package com.mfurmanczyk.toodoo.mobile.view.screen

import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ArrowBack
import androidx.compose.material.icons.twotone.Check
import androidx.compose.material.icons.twotone.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.mfurmanczyk.toodoo.mobile.R
import com.mfurmanczyk.toodoo.mobile.util.NavigationDestination
import com.mfurmanczyk.toodoo.mobile.util.NavigationType
import com.mfurmanczyk.toodoo.mobile.util.toComposeColor
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.TooDooTheme
import com.mfurmanczyk.toodoo.mobile.view.screen.theme.spacing
import com.mfurmanczyk.toodoo.mobile.viewmodel.CategoryEntryViewModel
import com.mfurmanczyk.toodoo.mobile.viewmodel.exception.InvalidCategoryNameException
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.MaterialDialogState
import com.vanpra.composematerialdialogs.color.ColorPalette
import com.vanpra.composematerialdialogs.color.colorChooser
import com.vanpra.composematerialdialogs.rememberMaterialDialogState

object CategoryEntryDestination : NavigationDestination(
    displayedTitle = R.string.category,
    route = "new_category"
) {
    const val parameterName = "categoryId"
    private const val categoryRoute = "category"

    val parametrizedRoute = "$categoryRoute/{$parameterName}"
    fun destinationWithParam(categoryId: Long) = "$categoryRoute/$categoryId"
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategoryEntryScreen(
    navController: NavHostController,
    navigationType: NavigationType,
    modifier: Modifier = Modifier
) {

    val viewModel = hiltViewModel<CategoryEntryViewModel>()
    val uiState by viewModel.uiState.collectAsState()
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    val dialogState = rememberMaterialDialogState()

    ColorPickerDialog(dialogState) { viewModel.updateCategoryColor(it) }

    Scaffold(
        modifier = modifier,
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    if(uiState.newEntry) Text(text = stringResource(id = R.string.new_category))
                    else Text(uiState.categoryName ?: "")
                },
                actions = {
                    IconButton(
                        onClick = {
                            try {
                                viewModel.saveCategory()
                                navController.navigateUp()
                            } catch (e: InvalidCategoryNameException) {
                                Toast.makeText(context, context.getText(e.displayMessage), Toast.LENGTH_SHORT).show()
                            }
                        }
                    ) {
                        Icon(imageVector = Icons.TwoTone.Check, contentDescription = null)
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.navigateUp()
                        }
                    ) {
                        Icon(
                            imageVector = if(navigationType != NavigationType.NAV_DRAWER) Icons.TwoTone.ArrowBack else Icons.TwoTone.Close,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    ) {
        CategoryEntryScreenContent(
            modifier = Modifier.padding(it),
            categoryName = uiState.categoryName ?: "",
            categoryColor = uiState.colorHolder.toComposeColor(),
            onColorClick = {
                focusManager.clearFocus()
                dialogState.show()
            },
            onCategoryNameChanged = viewModel::updateCategoryName
        )
    }



}

@Composable
private fun ColorPickerDialog(
    dialogState: MaterialDialogState,
    onColorSelected: (Color) -> Unit
) {
    MaterialDialog(
        dialogState = dialogState,
        elevation = MaterialTheme.spacing.medium,
        border = BorderStroke(1.dp, Color.Gray),
        autoDismiss = true,
        buttons = {
            negativeButton(
                text = "Cancel"
            )
            positiveButton(
                text = "Select"
            )
        }
    ) {
        colorChooser(
            colors = ColorPalette.Primary,
            subColors = ColorPalette.PrimarySub,
            onColorSelected = onColorSelected
        )
    }
}

@Composable
private fun CategoryEntryScreenContent(
    categoryName: String,
    categoryColor: Color,
    onColorClick: () -> Unit,
    onCategoryNameChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(horizontal = MaterialTheme.spacing.extraSmall),
            verticalArrangement = Arrangement.spacedBy(
                space = MaterialTheme.spacing.extraSmall,
                alignment = Alignment.Top
            )
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                maxLines = 1,
                label = {
                    Text(text = stringResource(R.string.category_name))
                },
                value = categoryName,
                onValueChange = onCategoryNameChanged
            )
            OutlinedButton(
                onClick = onColorClick,
                colors = ButtonDefaults.outlinedButtonColors(contentColor = categoryColor),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.pick_color))
            }
        }
    }
}

@Preview
@Composable
fun CategoryEntryScreenContentPreview() {
    TooDooTheme {
        CategoryEntryScreenContent(
            "",
            Color.Red,
            onColorClick = {},
            onCategoryNameChanged = {  }

        )
    }
}