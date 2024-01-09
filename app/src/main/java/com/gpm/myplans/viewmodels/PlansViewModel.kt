package com.gpm.myplans.viewmodels

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.domain.local.model.PlansEntity
import com.gpm.myplans.repository.PlansRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class PlansViewModel(private val plansRepository: PlansRepository) : ViewModel() {

    private var _plansDataList = MutableStateFlow<List<PlansEntity>>(emptyList())
    private val _loadingData = MutableStateFlow(false)

    var plansDataList: StateFlow<List<PlansEntity>> = _plansDataList.asStateFlow()
    var loadingData : StateFlow<Boolean> = _loadingData
    private var noteTitle: String = ""
    private var planEntity = PlansEntity()

    fun saveNoteTitle(title: String) {
        noteTitle = title
    }

    fun getNoteTitle() :String {
        return noteTitle;
    }

    fun setPlan(plan: PlansEntity) {
        planEntity = plan
    }

    fun getPlan() : PlansEntity {
        return planEntity
    }

    fun savePlan(list: List<PlanDetailsEntity>) {
        if (planEntity.id != 0) {
            upsertPlans(PlansEntity(id = planEntity.id, name = noteTitle, date = getFormattedDate(), planDetailsEntity = list))
        } else {
            upsertPlans(PlansEntity(name = noteTitle, date = getFormattedDate(), planDetailsEntity = list))
        }
    }

    init {
        getPlansList()
    }

    fun getPlansList() {
        viewModelScope.launch(Dispatchers.IO) {
            plansRepository.getPlansData()
                .onStart {  _loadingData.update { true } }
                .onCompletion {  _loadingData.update { false } }
                .collect {
                    _plansDataList.value = it
            }
        }
    }

    fun deletePlans(ids : List<Int>) {
        viewModelScope.launch(Dispatchers.IO) {
            plansRepository.clearPlansData(ids)
            getPlansList()
        }

    }

    fun updatePlanItems(plan: PlansEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            plansRepository.updatePlansItems(plan.name, plan.planDetailsEntity, plan.id)
            getPlansList()
        }

    }

    private fun upsertPlans(plansObject: PlansEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            plansRepository.insertPlansData(plansObject)
        }
    }

    private fun getFormattedDate(): String {
        return try {
            val formatter = SimpleDateFormat("dd MMM, yyyy", Locale.getDefault())
            formatter.format(Date())
        } catch (e: Exception) {
            Log.e("System out", e.printStackTrace().toString())
            Date().toString()
        }
    }


    fun replacePlanIds(planList: List<PlansEntity>, list: List<Int>): List<Int> {
        val idList = mutableListOf<Int>()
        viewModelScope.launch(Dispatchers.Default) {
            list.forEach {
                idList.add(planList[it].id)
            }
        }
        return idList
    }

    suspend fun removeItemIds(itemList: List<PlanDetailsEntity>, list: List<Int>) : List<PlanDetailsEntity> {
         val idList = itemList.toMutableList()
         val request = viewModelScope.launch (Dispatchers.Default) {
            list.forEach {
                idList.remove(itemList[it])
            }
        }
        request.join()
        return idList
    }

    fun updateItemList(itemList: List<PlanDetailsEntity>) {
        planEntity.planDetailsEntity = itemList
        viewModelScope.launch(Dispatchers.IO) {
            plansRepository.updatePlansItems(planEntity.name, planEntity.planDetailsEntity, planEntity.id)
        }
    }
}