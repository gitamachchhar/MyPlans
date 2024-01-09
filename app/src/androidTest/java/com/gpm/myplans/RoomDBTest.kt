package com.gpm.myplans

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.gpm.myplans.db.AppDatabase
import com.gpm.myplans.domain.local.dao.PlansDao
import com.gpm.myplans.domain.local.model.PlansEntity
import com.gpm.myplans.repository.PlansRepository
import com.gpm.myplans.viewmodels.PlansViewModel
import junit.framework.TestCase
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class RoomDBTest : TestCase() {

    private lateinit var database: AppDatabase
    private lateinit var plansDao: PlansDao
    private lateinit var plansViewModel: PlansViewModel
    @Before
    fun setUpRoomTest() {
        super.setUp()
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java).build()

        plansDao = database.plansDao

        val planRepo = Mockito.mock(PlansRepository::class.java)
        plansViewModel = PlansViewModel(planRepo)
    }

    @Test
    fun testInsertAndFetch() = runBlocking {
        plansDao.insertPlansData(
            PlansEntity(id = 1,
            name = "31st Party",
            date = plansViewModel.getFormattedDate(),
            planDetailsEntity = mutableListOf())
        )

        val list = plansDao.getPlansData()
        assertTrue(list.isNotEmpty())
    }

    @Test
    fun testEditAndFetch() = runBlocking {
        plansDao.insertPlansData(
            PlansEntity(id = 1,
                name = "New Year Party",
                date = plansViewModel.getFormattedDate(),
                planDetailsEntity = mutableListOf())
        )

        val list = plansDao.getPlansData().filter { it.id == 1 }
        assertTrue(list.isNotEmpty())
        assertTrue(list[0].name == "New Year Party")
    }

    @Test
    fun testDeleteAndFetch() = runBlocking {
        plansDao.clearPlansData(
            MutableList(1) { 1 }
        )

        val list = plansDao.getPlansData()
        assertTrue(list.isEmpty())
    }

    @After
    fun closeDB() {
        database.close()
    }
}