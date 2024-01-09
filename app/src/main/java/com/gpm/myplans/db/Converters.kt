package com.gpm.myplans.db

import android.util.Log
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.gpm.myplans.domain.local.model.PlanDetailsEntity
import java.lang.reflect.Type


open class Converters {

    @TypeConverter
    fun stringToPlanDetailsEntity(json: String): List<PlanDetailsEntity> {
        try {
            val gson = Gson()
            val type: Type = object : TypeToken<List<PlanDetailsEntity>>() {}.type
            return gson.fromJson(json, type)
        } catch (e: Exception) {
            Log.e("System out", e.printStackTrace().toString())
        }
        return emptyList()
    }

    @TypeConverter
    fun planDetailsEntityToString(list: List<PlanDetailsEntity>): String {
        try {
            val gson = Gson()
            val type: Type = object : TypeToken<List<PlanDetailsEntity>>() {}.type
            return gson.toJson(list, type)
        } catch (e: Exception) {
            Log.e("System out", e.printStackTrace().toString())
        }
        return ""
    }
}