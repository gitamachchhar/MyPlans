package com.gpm.myplans.ui.activity

import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.gpm.myplans.ui.compose.PlansListNavigation
import com.gpm.myplans.ui.theme.MyPlansTheme
import com.gpm.myplans.viewmodels.PlansViewModel
import org.koin.androidx.compose.viewModel

class MainActivity : ComponentActivity() {

    private var state = true
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        displaySplashScreen()
        enableEdgeToEdge()
        setContent {
            MyPlansTheme {
                val plansViewModel: PlansViewModel by viewModel()
                PlansListNavigation(
                    plansViewModel = plansViewModel
                )
            }
        }
    }

    private fun displaySplashScreen() {
        timer()
        val splashScreen = installSplashScreen()
        splashScreen.setKeepOnScreenCondition {
            state
        }
    }

    private fun timer() {
        val timer = object: CountDownTimer(1500, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                state = false
            }
        }
        timer.start()
    }

}



