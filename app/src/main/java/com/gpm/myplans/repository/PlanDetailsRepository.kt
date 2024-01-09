package com.gpm.myplans.repository

import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import kotlinx.coroutines.flow.Flow

interface PlanDetailsRepository {
    fun getPlanDetailsData(planId: Int): Flow<List<PlanDetailsEntity>>
    suspend fun insertPlanDetailsData(planDetailsObject: PlanDetailsEntity)
    suspend fun updatePlansName(planName: String, id: Int)
    suspend fun clearPlanDetailsData(ids : List<Int>)
}