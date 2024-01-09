package com.gpm.myplans

import androidx.activity.ComponentActivity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.performClick
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.gpm.myplans.ui.compose.PlansListNavigation
import com.gpm.myplans.ui.compose.PlansScreen
import com.gpm.myplans.viewmodels.PlansViewModel
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.androidx.compose.inject

@RunWith(AndroidJUnit4::class)
class MyPlansNavigationTest {
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.gpm.myplans", appContext.packageName)
    }

    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController


    @Before
    fun setupPlansListNavHost() {
        composeTestRule.setContent {
            val viewModel : PlansViewModel by inject()
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            PlansListNavigation(navController = navController, plansViewModel = viewModel)
        }
    }

    @Test
    fun plansListNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(PlansScreen.PlanList.name)
    }

    @Test
    fun displayAlertDialog() {
        composeTestRule.onNodeWithStringId(R.string.new_note).performClick()
        composeTestRule.onNode(hasTestTag("EnterNote"), useUnmergedTree = true).assertIsDisplayed()
    }

    @Test
    fun navigateToPlanDetailsScreen() {
        displayAlertDialog()
        composeTestRule.onNode(hasTestTag("OK"), useUnmergedTree = true).performClick()
        navController.assertCurrentRouteName(PlansScreen.PlanDetails.name)
    }

    @Test
    fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
    }

    @Test
    fun navHost_clickBackOnPlanDetailsScreen_navigatesToPlansListScreen() {
        navigateToPlanDetailsScreen()
        performNavigateUp()
        navController.assertCurrentRouteName(PlansScreen.PlanList.name)
    }
}