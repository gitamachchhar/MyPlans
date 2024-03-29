package com.gpm.myplans.ui.compose

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import com.gpm.myplans.R
import com.gpm.myplans.domain.local.model.AppBarState
import com.gpm.myplans.domain.local.model.PlansEntity
import com.gpm.myplans.ui.compose.common.AlertDialog
import com.gpm.myplans.ui.compose.common.ComposeProgressBar
import com.gpm.myplans.ui.compose.common.CustomAppBar
import com.gpm.myplans.ui.compose.common.EmptyState
import com.gpm.myplans.ui.compose.common.rememberLifecycleEvent
import com.gpm.myplans.ui.theme.PurpleGrey40
import com.gpm.myplans.ui.theme.PurpleLight
import com.gpm.myplans.ui.theme.labelMedium
import com.gpm.myplans.ui.theme.labelSmall
import com.gpm.myplans.viewmodels.ActionEventsViewModel
import com.gpm.myplans.viewmodels.PlansViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.androidx.compose.viewModel

private var selectedPlans = mutableListOf<Int>()

@OptIn(ExperimentalComposeUiApi::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun PlansListScreen(
    onComposing: (AppBarState) -> Unit,
    viewModel: PlansViewModel,
    onNextButtonClicked: () -> Unit = {},
) {


    // user action viewmodel
    val actionButtonViewModel: ActionEventsViewModel by viewModel()
    val lifecycleEvent = rememberLifecycleEvent()

    // dialog handler
    var dialogVisibility = actionButtonViewModel.getDialogVisibility().collectAsState()
    var isDialogVisible by remember { mutableStateOf(dialogVisibility) }

    val loading = viewModel.loadingData.collectAsState()
    var loadingState by remember { mutableStateOf(loading) }
    var plans = viewModel.plansDataList.collectAsState()
    var planList by remember { mutableStateOf(plans) }

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_RESUME) {
            viewModel.getPlansList()
        }
    }

    CustomAppBar(onComposing = onComposing,
        title = stringResource(id = R.string.plans),
        navIcon = R.drawable.lists,
        onEdit = {
            actionButtonViewModel.setDialogVisibility(true) },
        onDelete = {
            viewModel.deletePlans(viewModel.replacePlanIds(planList.value, selectedPlans))
            actionButtonViewModel.resetAllActionButtons(false)
        })

    Scaffold(
        floatingActionButtonPosition = FabPosition.End,
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    viewModel.setPlan(PlansEntity())
                    actionButtonViewModel.setDialogVisibility(true) },
                icon = { Icon(Icons.Filled.Add, contentDescription = "") },
                text = { Text(text = stringResource(id = R.string.new_note)) }
            )
        },
        modifier = Modifier.semantics {
            testTagsAsResourceId = true
        }
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (planList.value.isNotEmpty()) {
                PlansList(
                    modifier = Modifier
                        .align(Alignment.TopCenter)
                        .semantics {
                            testTagsAsResourceId = true
                        },
                    list = planList.value,
                    onNextButtonClicked = onNextButtonClicked,
                    viewModel = viewModel,
                    actionButtonViewModel = actionButtonViewModel,
                )
            } else {
                EmptyState(
                    stringResource(id = R.string.empty_state_msg),
                    stateImage = R.drawable.empty_state
                )
            }
            if (loadingState.value) {
                ComposeProgressBar()
            }
        }

        if (isDialogVisible.value) {
            AlertDialog(
                title = stringResource(id = R.string.enter_title),
                content = stringResource(id = R.string.title),
                onClick = {
                if (actionButtonViewModel.getEditMenuState().value) {
                    CoroutineScope(Dispatchers.Default).launch {
                        Log.e("System out", "Yes 0")
                        val plan = viewModel.getPlanById(planList.value[selectedPlans[0]].id)
                        Log.e("System out", "Yes 4")
                        plan.name = actionButtonViewModel.getDialogText().value
                        viewModel.updatePlanItems(plan)
                        actionButtonViewModel.run {
                            setDialogText("")
                            setItemSelectionState(false)
                            resetAllActionButtons(false)
                        }
                    }
                } else {
                    viewModel.saveNoteTitle(actionButtonViewModel.getDialogText().value)
                    actionButtonViewModel.setDialogText("")
                    onNextButtonClicked()
                }
            })
        }
    }
}

@Composable
private fun PlansList(
    modifier: Modifier = Modifier,
    list: List<PlansEntity> = emptyList(),
    onNextButtonClicked: () -> Unit = {},
    viewModel: PlansViewModel,
    actionButtonViewModel: ActionEventsViewModel
) {

    var selectedItems by remember { mutableStateOf<Set<Int>>(emptySet()) }
    var selectAll by remember { mutableStateOf(false) }
    val isItemSelectionEnabled = actionButtonViewModel.getItemSelectionState().collectAsState()
    var isEnabled by remember { mutableStateOf(isItemSelectionEnabled) }

    LaunchedEffect(key1 = selectAll) {
        selectedItems = if (selectAll)
            selectedItems.plus(list.indices)
        else
            selectedItems.minus(list.indices)

        selectedPlans = selectedItems.toMutableList()
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
                        actionButtonViewModel.run {
                            setItemSelectionState(it)
                            setSelectDeleteMenusState(it)
                            setEditMenuState(false)
                        }
                    },
                    role = Role.Checkbox
                )
        ) {
            Text(text = stringResource(R.string.selectall), modifier = Modifier.weight(1f))
            Checkbox(checked = selectAll, onCheckedChange = null)
        }

        LazyColumn(
            modifier = modifier
                .fillMaxSize()
                .testTag("lc_myplans"),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            itemsIndexed(list) { index, plan ->
                PlansRowItem(
                    plan = plan,
                    onNextButtonClicked = onNextButtonClicked,
                    viewModel = viewModel,
                    isEnabled = isEnabled.value,
                    selectedItem = selectedItems.contains(index),
                    onClick = {
                            selectedItems =
                                if (selectedItems.contains(index))
                                    selectedItems.minus(index)
                                else
                                    selectedItems.plus(index)

                            selectedPlans = selectedItems.toMutableList()

                            if (selectedItems.isEmpty()) {
                                selectAll = false
                                actionButtonViewModel.setItemSelectionState(false)
                            } else if (selectedItems.size == list.size) {
                                selectAll = true
                            }
                            actionButtonViewModel.run {
                                setEditMenuState(selectedItems.size == 1)
                                setSelectDeleteMenusState(selectedItems.isNotEmpty())
                            }
                    },
                    onEnableChange = actionButtonViewModel::setItemSelectionState
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PlansRowItem(
    plan: PlansEntity,
    onNextButtonClicked: () -> Unit = {},
    viewModel: PlansViewModel,
    isEnabled: Boolean = false,
    onEnableChange: (Boolean) -> Unit,
    selectedItem: Boolean,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag(stringResource(id = R.string.list_to_details_tag))
            .combinedClickable(onLongClick = {
                viewModel.saveNoteTitle(plan.name)
                viewModel.setPlan(plan)
                onEnableChange(true)
            }, onClick = {
                viewModel.saveNoteTitle(plan.name)
                viewModel.setPlan(plan)
                onNextButtonClicked()
            }),
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = PurpleLight),
        border = BorderStroke(0.dp, PurpleLight),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)

    ) {

        Row(
            modifier = Modifier
                .padding(12.dp)
                .wrapContentWidth()
                .clickable { onClick.invoke() }
                ,
            verticalAlignment = Alignment.CenterVertically
        ) {

            if (isEnabled) {
                Checkbox(
                    checked = selectedItem, onCheckedChange = null, modifier = Modifier
                        .padding(10.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier
                    .wrapContentWidth()
            ) {
                Text(
                    text = plan.name,
                    style = labelMedium
                )
                Text(
                    text = plan.date,
                    style = labelSmall.merge(color = PurpleGrey40)
                )
            }
        }
    }
}
