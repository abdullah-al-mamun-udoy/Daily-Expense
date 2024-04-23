package com.example.expensenote.navigation

sealed class Screen(val route: String) {

    data object HomeScreen : Screen("home_screen")
    data object AnalysisScreen : Screen("analysis_screen")
    data object SettingScreen : Screen("setting_screen")
    data object CalendarScreen : Screen("calendar_screen")
    data object SplashScreen : Screen("splash_screen")
    data object OnBoardingScreen : Screen("onboarding_screen")

}