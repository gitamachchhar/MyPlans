package com.gpm.myplans.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.gpm.myplans.domain.local.dao.PlansDao
import com.gpm.myplans.domain.local.model.PlansEntity

private const val DATABASE_VERSION = 1
const val APP_DATABASE_NAME = "plans_db"

@Database(
    entities = [PlansEntity::class],
    version = DATABASE_VERSION,
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract val plansDao : PlansDao
}