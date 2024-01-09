package com.gpm.myplans.ui.compose.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.gpm.myplans.R
import com.gpm.myplans.ui.theme.labelSmall
import com.gpm.myplans.viewmodels.ActionEventsViewModel
import kotlinx.coroutines.delay
import org.koin.androidx.compose.viewModel

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AlertDialog(
    onClick: () -> Unit = {},
    title: String = "",
    content : String = ""
) {

    val actionButtonViewModel: ActionEventsViewModel by viewModel()

    // dialog handler
    var itemName = actionButtonViewModel.getDialogText().collectAsState()
    var listItemName by remember { mutableStateOf(itemName) }

    // edittext focus
    val focusRequester = remember { FocusRequester() }
    val keyboard = LocalSoftwareKeyboardController.current

    LaunchedEffect(focusRequester) {
        delay(100)
        focusRequester.requestFocus()
        keyboard?.show()
    }
    MyPlanItemDialog(
        title = {
            Text(
                modifier = Modifier.testTag("EnterNote"),
                text = title, //stringResource(id = R.string.enter_title),
                style = labelSmall
            )
        },
        content = {
            OutlinedTextField(
                modifier = Modifier
                    .focusRequester(focusRequester),
                value = listItemName.value,
                onValueChange = { actionButtonViewModel.setDialogText(it) },
                label = { Text(text = content) },
            )
        },
        dismissButton = {
            TextButton(
                onClick = { actionButtonViewModel.setDialogVisibility(false) },
                content = { Text(text = stringResource(id = R.string.cancel)) },
            )
        },
        confirmButton = {
            TextButton(
                modifier = Modifier.testTag(stringResource(id = R.string.ok)),
                onClick = {
                    onClick.invoke()
                    actionButtonViewModel.setDialogVisibility(false)
                },
                content = { Text(text = stringResource(id = R.string.ok)) },
            )
        },
        onDismiss = {
            actionButtonViewModel.setDialogVisibility(false)
        },
    )
}


@Composable
fun MyPlanItemDialog(
    title: @Composable () -> Unit,
    content: @Composable () -> Unit,
    dismissButton: @Composable () -> Unit,
    confirmButton: @Composable () -> Unit,
    onDismiss: () -> Unit,
) {
    Dialog(onDismiss) {
        Surface(shape = MaterialTheme.shapes.medium) {
            Column {
                Column(Modifier.padding(24.dp)) {
                    title.invoke()
                    Spacer(Modifier.size(16.dp))
                    content.invoke()
                }
                Spacer(Modifier.size(4.dp))
                Row(
                    Modifier.padding(8.dp).fillMaxWidth(),
                    Arrangement.spacedBy(8.dp, Alignment.End),
                ) {
                    dismissButton.invoke()
                    confirmButton.invoke()
                }
            }
        }
    }
}