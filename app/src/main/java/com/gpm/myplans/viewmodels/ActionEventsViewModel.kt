package com.gpm.myplans.viewmodels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ActionEventsViewModel : ViewModel() {

    private var actionButtonsVisible = MutableStateFlow(false)
    private var showEditMenu = MutableStateFlow(false)
    private var isDialogVisible = MutableStateFlow(false)
    private var listItemName = MutableStateFlow("")
    private var itemSelectionState = MutableStateFlow(false)

    fun setItemSelectionState(isActive: Boolean) {
        itemSelectionState.value = isActive
    }

    fun getItemSelectionState(): StateFlow<Boolean> {
        return itemSelectionState
    }

    fun setSelectDeleteMenusState(isActive: Boolean) {
        actionButtonsVisible.value = isActive
    }

    fun getSelectDeleteMenusState(): StateFlow<Boolean> {
       return actionButtonsVisible
    }

    fun setEditMenuState(isActive: Boolean) {
        showEditMenu.value = isActive
    }

    fun getEditMenuState() : StateFlow<Boolean> {
        return showEditMenu
    }

    fun setDialogVisibility(isVisible: Boolean) {
        isDialogVisible.value = isVisible
    }

    fun getDialogVisibility() : StateFlow<Boolean> {
        return isDialogVisible
    }

    fun setDialogText(text: String) {
        listItemName.value = text
    }

    fun getDialogText() : StateFlow<String> {
        return listItemName
    }

    fun resetAllActionButtons(isVisible: Boolean) {
        showEditMenu.value = isVisible
        actionButtonsVisible.value = isVisible
    }
}
sealed interface MainActivityUiState {
    data object Loading : MainActivityUiState
}

