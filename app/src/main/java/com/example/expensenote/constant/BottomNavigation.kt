package com.example.expensenote.constant

import androidx.annotation.DrawableRes
import com.example.expensenote.R
import com.example.expensenote.navigation.Screen

enum class BottomNavigation(
    @DrawableRes val icon: Int,
    val route: String?,
    val level: String
) {
    Home(
        R.drawable.ic_records,
        Screen.HomeScreen.route,
        level="Records"
    ),
    Analysis(
        R.drawable.ic_analysis,
        Screen.AnalysisScreen.route,
        level="Analytics"
    ),
    Calendar(
        R.drawable.ic_calendar,
        Screen.CalendarScreen.route,
        level ="Calendar"
    ),
    Setting(
        R.drawable.ic_settings,
        Screen.SettingScreen.route,
        level ="Settings"
    )
}