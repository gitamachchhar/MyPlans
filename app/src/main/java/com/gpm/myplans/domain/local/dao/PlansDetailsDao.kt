package com.gpm.myplans.domain.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.gpm.myplans.domain.local.model.PlanDetailsEntity

@Dao
interface PlansDetailsDao {

    @Query("SELECT * FROM PlansDetails where id = :planId")
    suspend fun getPlanDetailsData(planId: Int) : List<PlanDetailsEntity>

    @Upsert
    suspend fun insertPlanDetailsData(planDetailsData: PlanDetailsEntity)

    @Query("UPDATE PlansDetails SET name = :name WHERE id =:id")
    suspend fun updatePlanDetailsName(name: String, id: Int)

    @Query("DELETE FROM PlansDetails where id in (:ids)")
    suspend fun clearPlanDetailsData(ids : List<Int>)

}