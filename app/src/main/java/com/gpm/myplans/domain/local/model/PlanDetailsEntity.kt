package com.gpm.myplans.domain.local.model

//import androidx.room.ColumnInfo
//import androidx.room.Entity
//import androidx.room.ForeignKey
//import androidx.room.PrimaryKey
//
//@Entity(tableName = "PlansDetails", foreignKeys = [ForeignKey(
//    entity = PlansEntity::class,
//    parentColumns = arrayOf("id"),
//    childColumns = arrayOf("planId"),
//    onDelete = ForeignKey.CASCADE
//)])
//
//data class PlanDetailsEntity (
//
//    @PrimaryKey(autoGenerate = true)
//    @ColumnInfo(name = "id")
//    var id : Int = 0,
//
//    @ColumnInfo(name = "planId")
//    var planId : Int = 0,
//
//    @ColumnInfo(name = "name")
//    var name : String,
//)

data class PlanDetailsEntity(var id : Int = 0, var name : String)