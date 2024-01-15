package com.gpm.myplans.domain.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import com.gpm.myplans.domain.local.model.PlansEntity

@Dao
interface PlansDao {

    @Query("SELECT * FROM Plans ORDER BY id DESC")
    fun getPlansData() : List<PlansEntity>
    @Query("SELECT * FROM Plans Where id in (:id)")
    fun getPlanById(id: Int) : PlansEntity
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPlansData(plansData: PlansEntity)

    @Query("UPDATE Plans SET name = :name, planItems = :planItems WHERE id =:id")
    suspend fun updatePlanItems(name: String, planItems: List<PlanDetailsEntity>, id: Int)

    @Query("DELETE FROM Plans where id in (:ids)")
    suspend fun clearPlansData(ids : List<Int>)

}