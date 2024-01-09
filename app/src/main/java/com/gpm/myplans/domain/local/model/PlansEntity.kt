package com.gpm.myplans.domain.local.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "Plans")
data class PlansEntity(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id : Int = 0,

    @ColumnInfo(name = "name")
    var name : String = "My Notes",

    @ColumnInfo(name = "date")
    var date : String = "",

    @ColumnInfo(name = "planItems")
    var planDetailsEntity: List<PlanDetailsEntity> = emptyList()

)