package com.gpm.myplans.datasource

import com.gpm.myplans.domain.local.dao.PlansDetailsDao
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.repository.PlanDetailsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlansDetailsDataSource(private val planDetailsDao: PlansDetailsDao) : PlanDetailsRepository {
    override fun getPlanDetailsData(planId: Int): Flow<List<PlanDetailsEntity>> = flow {
        val planList = planDetailsDao.getPlanDetailsData(planId)
        emit(planList)
    }

    override suspend fun insertPlanDetailsData(planDetailsObject: PlanDetailsEntity) {
        planDetailsDao.insertPlanDetailsData(planDetailsObject)
    }

    override suspend fun updatePlansName(planName: String, id: Int) {
        planDetailsDao.updatePlanDetailsName(planName, id)
    }

    override suspend fun clearPlanDetailsData(ids : List<Int>) {
        planDetailsDao.clearPlanDetailsData(ids)
    }

}