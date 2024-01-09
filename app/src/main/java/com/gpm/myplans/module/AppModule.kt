package com.gpm.myplans.module

import androidx.room.Room
import com.gpm.myplans.datasource.PlansDataSource
import com.gpm.myplans.db.APP_DATABASE_NAME
import com.gpm.myplans.db.AppDatabase
import com.gpm.myplans.domain.local.dao.PlansDao
import com.gpm.myplans.repository.PlansRepository
import com.gpm.myplans.viewmodels.ActionEventsViewModel
import com.gpm.myplans.viewmodels.PlansViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            AppDatabase::class.java,
            APP_DATABASE_NAME,
        ).build()
    }

    single<PlansDao> {
        val database = get<AppDatabase>()
        database.plansDao
    }

    single<PlansRepository> {
        PlansDataSource(get())
    }

    viewModel {
        PlansViewModel(get())
    }
    viewModel {
        ActionEventsViewModel()
    }

}
