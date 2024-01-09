package com.gpm.myplans.datasource

import com.gpm.myplans.domain.local.dao.PlansDao
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.domain.local.model.PlansEntity
import com.gpm.myplans.repository.PlansRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlansDataSource(private val planDao: PlansDao) : PlansRepository {

    override fun getPlansData(): Flow<List<PlansEntity>> = flow {
        emit(planDao.getPlansData())
    }

    override suspend fun insertPlansData(plansObject: PlansEntity) {
       return planDao.insertPlansData(plansObject)
    }

    override suspend fun updatePlansItems(planName: String, planItems: List<PlanDetailsEntity>, id: Int) {
        planDao.updatePlanItems(planName, planItems, id)
    }

    override suspend fun clearPlansData(ids : List<Int>)  {
        planDao.clearPlansData(ids)
    }

}