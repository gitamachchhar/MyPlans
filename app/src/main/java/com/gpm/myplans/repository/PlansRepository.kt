package com.gpm.myplans.repository

import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.domain.local.model.PlansEntity
import kotlinx.coroutines.flow.Flow

interface PlansRepository {
    fun getPlansData(): Flow<List<PlansEntity>>
    suspend fun insertPlansData(plansObject: PlansEntity)
    suspend fun updatePlansItems(planName: String, planItems: List<PlanDetailsEntity>, id: Int)
    suspend fun clearPlansData(ids : List<Int>)
}