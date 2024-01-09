package com.gpm.myplans.ui.activity

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.gpm.myplans.ui.compose.PlansListNavigation
import com.gpm.myplans.ui.theme.MyPlansTheme
import com.gpm.myplans.viewmodels.PlansViewModel
import org.koin.androidx.compose.viewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyPlansTheme {
                val plansViewModel: PlansViewModel by viewModel()
                PlansListNavigation(
                    plansViewModel = plansViewModel
                )
            }
        }
    }
}






