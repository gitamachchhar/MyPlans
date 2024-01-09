package com.gpm.myplans.ui.compose

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gpm.myplans.R
import com.gpm.myplans.domain.local.model.AppBarState
import com.gpm.myplans.ui.compose.common.ComposeAppBar
import com.gpm.myplans.viewmodels.PlansViewModel

/**
 * enums to represent the screens
 */
enum class PlansScreen(@StringRes val title: Int) {
    PlanList(title = R.string.plans),
    PlanDetails(title = R.string.plan_details),
}

/**
 * Composable to handle navigation between screens
 */

@Composable
fun PlansListNavigation(
    plansViewModel: PlansViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

    var appBarState by remember { mutableStateOf(AppBarState()) }

    Scaffold(
        topBar = {
            ComposeAppBar(
                currentScreen = appBarState.title,
                resource = appBarState.navIcon,
                actions = {
                    appBarState.actions?.invoke(this)
                }
            )
        }) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = PlansScreen.PlanList.name,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(route = PlansScreen.PlanList.name) {
                PlansListScreen(
                    onComposing = {  appBarState = it },
                    viewModel = plansViewModel,
                    onNextButtonClicked = {
                        navController.navigate(PlansScreen.PlanDetails.name)
                    })
            }
            composable(route = PlansScreen.PlanDetails.name) {
                PlanDetailsScreen(
                    onComposing = {  appBarState = it },
                    viewModel = plansViewModel,
                    onBackButtonClicked = {
                        navController.popBackStack(PlansScreen.PlanList.name, inclusive = false)
                    })
            }
        }
    }
}
