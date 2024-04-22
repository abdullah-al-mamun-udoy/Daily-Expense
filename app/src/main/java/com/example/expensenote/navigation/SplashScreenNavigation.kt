package com.example.expensenote.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.expensenote.presentation.screen.main.MainScreen
import com.example.expensenote.presentation.screen.onboarding.OnBoardingScreen
import com.example.expensenote.presentation.screen.splash.SplashScreen


@Composable
fun SplashScreenNavigation(navHostController: NavHostController) {

    NavHost(
        navController = navHostController,
        route = Graph.SplashGraph.route,
        startDestination = Screen.SplashScreen.route,
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
    ) {

        composable(Screen.SplashScreen.route) {
            SplashScreen(navHost = navHostController)
        }
        composable(Screen.OnBoardingScreen.route) {
            OnBoardingScreen(navhost= navHostController)
        }
        composable(Graph.BottomGraph.route) {
            MainScreen(navHostController = rememberNavController())
        }

    }


}