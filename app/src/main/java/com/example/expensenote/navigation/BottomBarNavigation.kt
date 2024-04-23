package com.example.expensenote.navigation

import HomeScreen
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.expensenote.presentation.screen.AnalysisScreen.AnalysisScreen

import com.example.expensenote.presentation.screen.setting.SettingScreen


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun BottomBarNavigation(navHostController: NavHostController) {

    NavHost(
        navController = navHostController,
        route = Graph.BottomGraph.route,
        startDestination = Screen.HomeScreen.route,
        enterTransition = {
            fadeIn(tween(300))
        },
        exitTransition = {
            fadeOut(tween(300))
        },
        popEnterTransition = {
            fadeIn(tween(300))
        },
        popExitTransition = {
            fadeOut(tween(300))
        }
    )
    {
        composable(Screen.HomeScreen.route) {
            HomeScreen(navhost = navHostController)
        }
        composable(Screen.AnalysisScreen.route){
            AnalysisScreen(navHost = navHostController)
        }
        composable(Screen.SettingScreen.route) {
            SettingScreen(navhost = navHostController)
        }

    }


}

