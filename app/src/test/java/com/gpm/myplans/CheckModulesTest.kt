package com.gpm.myplans

import androidx.test.core.app.ApplicationProvider
import com.gpm.myplans.module.appModule
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.test.KoinTest
import org.koin.test.check.checkKoinModules
import org.koin.test.check.checkModules
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@Config(sdk = [30])
@RunWith(RobolectricTestRunner::class)
class CheckModulesTest : KoinTest {

    @Before
    fun before() {
        startKoin {
            androidContext(ApplicationProvider.getApplicationContext())
            modules(appModule)
        }
    }

    @Test
    fun `Verify Koin Configuration`() {
        checkModules {
            checkKoinModules(listOf(appModule))
        }
    }

    @After
    fun tearDown() {
        stopKoin()
    }

}