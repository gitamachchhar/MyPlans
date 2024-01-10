package com.gpm.myplans.ui.compose.common

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import com.gpm.myplans.R
import com.gpm.myplans.domain.local.model.AppBarState
import com.gpm.myplans.ui.theme.Purple40
import com.gpm.myplans.ui.theme.titleLarge
import com.gpm.myplans.viewmodels.ActionEventsViewModel
import org.koin.androidx.compose.viewModel

/**
 * Composable to display the topBar, navigation button, and action buttons.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeAppBar(
    currentScreen: String,
    resource: @Composable () -> Unit,
    actions: (@Composable RowScope.() -> Unit) = {}
) {

    TopAppBar(
        modifier = Modifier.fillMaxWidth().wrapContentHeight(),
        title = { Text(
            text = currentScreen,
            style = titleLarge
        ) },
        colors = TopAppBarDefaults.topAppBarColors(containerColor = Purple40),
        navigationIcon = resource,
        actions = actions
    )
}



@Composable
fun CustomAppBar(onComposing: (AppBarState) -> Unit,
                 title: String,
                 navIcon: Int,
                 onEdit: () -> Unit,
                 onDelete: () -> Unit,
                 navigateUp: () -> Unit = {},
                 contentDescription: String = "",) {

    // user action viewmodel
    val actionButtonViewModel: ActionEventsViewModel by viewModel()

    // app bar action button
    var sndState = actionButtonViewModel.getSelectDeleteMenusState().collectAsState()
    var editState = actionButtonViewModel.getEditMenuState().collectAsState()
    var selectDeleteMenusState by remember { mutableStateOf(sndState) }
    var editMenuState by remember { mutableStateOf(editState) }

    LaunchedEffect(key1 = true) {
        onComposing (
            AppBarState(
                title = title,
                navIcon = {
                    IconButton(onClick = { navigateUp.invoke() }) {
                        Icon(
                            painter = painterResource(id = navIcon),
                            contentDescription = contentDescription,
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    if (editMenuState.value) {
                        IconButton(onClick = { onEdit.invoke() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.edit_note),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                    if (selectDeleteMenusState.value) {
                        IconButton(onClick = { onDelete.invoke() }) {
                            Icon(
                                painter = painterResource(id = R.drawable.delete_forever),
                                contentDescription = "",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        )
    }
}

//@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
//@Composable
//fun GetWindowSize() {
//    val windowSizeClass = calculateWindowSizeClass(this)
//    //WindowSize.screenWidth = windowSizeClass.widthSizeClass
//    // WindowSize.screenHeight = windowSizeClass.heightSizeClass
//}