package com.gpm.myplans

import android.app.Application
import com.gpm.myplans.module.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class MyPlanApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MyPlanApplication)
            modules(listOf(appModule))
        }
    }
}