package com.gpm.myplans.ui.compose

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.twotone.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.gpm.myplans.R
import com.gpm.myplans.domain.local.model.AppBarState
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.ui.compose.common.AlertDialog
import com.gpm.myplans.ui.compose.common.CustomAppBar
import com.gpm.myplans.ui.theme.FFFFFFFF
import com.gpm.myplans.ui.theme.labelMedium
import com.gpm.myplans.viewmodels.ActionEventsViewModel
import com.gpm.myplans.viewmodels.PlansViewModel
import kotlinx.coroutines.launch

private var selectedItemIds = emptyList<Int>()

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlanDetailsScreen(
    onComposing: (AppBarState) -> Unit,
    viewModel: PlansViewModel = viewModel(),
    onBackButtonClicked: () -> Unit = {},
) {
    // user action viewmodel
    val actionButtonViewModel: ActionEventsViewModel by org.koin.androidx.compose.viewModel()

    // dialog handler
    var dialogVisibility = actionButtonViewModel.getDialogVisibility().collectAsState()
    var isDialogVisible by remember { mutableStateOf(dialogVisibility) }

    val plan = viewModel.getPlan()
    // get selected plan details
    var planItemList = remember {
        plan.planDetailsEntity.toMutableStateList()
    }

    val coroutineScope = rememberCoroutineScope()

    CustomAppBar(onComposing = onComposing,
        title = viewModel.getNoteTitle(),
        navIcon = R.drawable.arrow_back,
        contentDescription = stringResource(R.string.back_button),
        navigateUp = {
                        viewModel.savePlan(planItemList)
                        onBackButtonClicked()
                     },
        onEdit = {},
        onDelete = {
            coroutineScope.launch {
                val list = viewModel.removeItemIds(planItemList, selectedItemIds)
                viewModel.updateItemList(list)
                planItemList = list.toMutableStateList()
                actionButtonViewModel.resetAllActionButtons(false)
            }
        })

    BackHandler {
        viewModel.savePlan(planItemList)
        onBackButtonClicked()
    }
    // create UI
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = {
            FloatingActionButton(
                onClick = { actionButtonViewModel.setDialogVisibility(true) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            PlansDetailsList(
                list = planItemList.toList(),
                actionButtonViewModel = actionButtonViewModel
            )
        }

        if (isDialogVisible.value) {
            AlertDialog(
                title = stringResource(id = R.string.item_title),
                content = stringResource(id = R.string.item_name),
                onClick = {
                planItemList.add(
                    PlanDetailsEntity(
                        (planItemList.size + 1),
                        actionButtonViewModel.getDialogText().value
                    )
                )
                actionButtonViewModel.setDialogVisibility(false)
                actionButtonViewModel.setDialogText("")
            })
        }
    }
}

@Composable
private fun PlansDetailsList(
    list: List<PlanDetailsEntity> = emptyList(),
    actionButtonViewModel : ActionEventsViewModel
) {
    var isEnabled by remember { mutableStateOf(false) }
    var selectedItems by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectAll by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = selectAll) {
        selectedItems = if (selectAll)
            selectedItems.plus(list.indices)
        else
            selectedItems.minus(list.indices)

        selectedItemIds = selectedItems.toList()
    }
    Column {

        Row(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .toggleable(
                    value = selectAll,
                    onValueChange = {
                        selectAll = it
                        isEnabled = it
                        actionButtonViewModel.setSelectDeleteMenusState(it)
                    },
                    role = Role.Checkbox
                )
        ) {
            if (list.isNotEmpty()) {
                Text(text = stringResource(R.string.selectall), modifier = Modifier.weight(1f))
                Checkbox(checked = selectAll, onCheckedChange = null)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (list.isNotEmpty()) {
                itemsIndexed(list) {index, it ->
                    PlansDetailsRowItem(it,
                        actionButtonViewModel,
                        isEnabled = isEnabled,
                        selectedItem = selectedItems.contains(index),
                        onClick = {
                            selectedItems =
                                if (selectedItems.contains(index)) selectedItems.minus(index)
                                else selectedItems.plus(index)

                            if (selectedItems.isEmpty()) {
                                selectAll = false
                            } else if (selectedItems.size == list.size) selectAll = true

                            selectedItemIds = selectedItems.toMutableList()

                            actionButtonViewModel.setEditMenuState(false)
                            actionButtonViewModel.setSelectDeleteMenusState(selectedItems.isNotEmpty())
                        },
                        onEnableChange = { value ->
                            isEnabled = value
                        },
                        )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlansDetailsRowItem(plan: PlanDetailsEntity,
                        actionButtonViewModel : ActionEventsViewModel,
                        isEnabled: Boolean = false,
                        onEnableChange: (Boolean) -> Unit,
                        selectedItem: Boolean,
                        onClick: () -> Unit,) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, FFFFFFFF),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        val checked = remember { mutableStateOf(false) }
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
                .combinedClickable(onLongClick = {
                    onEnableChange(true)
                }, onClick = onClick),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isEnabled) {
                Checkbox(
                    checked = selectedItem, onCheckedChange = null, modifier = Modifier
                        .padding(10.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = plan.name,
                style = labelMedium
            )
        }
    }
}