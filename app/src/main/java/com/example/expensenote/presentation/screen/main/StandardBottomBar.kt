package com.example.expensenote.presentation.screen.main

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.expensenote.R
import com.example.expensenote.constant.BottomNavigation
import com.example.expensenote.navigation.Screen
import com.example.expensenote.presentation.screen.CalendarScreen.CalendarViewModel
import com.example.expensenote.ui.theme.appColor
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import com.maxkeppeler.sheets.calendar.models.CalendarTimeline
import java.time.LocalDate


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StandardBottomAppBar(
    navController: NavController,
) {

    val navBackStackEntry = navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry.value?.destination


    BottomAppBar(
        backgroundColor = appColor.copy(alpha = .8f),
        elevation = 0.dp

    ) {
        val context = LocalContext.current
        val selectedDateRange =
            remember { mutableStateOf<Pair<LocalDate?, LocalDate?>>(null to null) }
        val calendarState = rememberSheetState()
        val date = CalendarDialog(
            state = calendarState,

            selection = CalendarSelection.Period { startDate, endDate ->
                selectedDateRange.value = startDate to endDate
            }, CalendarConfig(monthSelection = true, yearSelection = true, maxYear = 2024, disabledTimeline = CalendarTimeline.FUTURE  )
        )

        BottomNavigation.entries.forEach {
            BottomNavigationItem(

                selected = currentDestination?.hierarchy?.any { it.route == it.route } == true,
                onClick = {
                    it.route?.let { it1 ->
                        if (it.route == Screen.CalendarScreen.route) {
                            calendarState.show()

                        } else {
                            navController.navigate(it1) {
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    }

                },
                icon = {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Image(
                            painter = painterResource(id = it.icon),
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                        Text(
                            text = it.level,
                            color = Color.White
                        )
                    }
                }
            )
            LaunchedEffect(selectedDateRange.value) {
                val (startDate, endDate) = selectedDateRange.value
                if (startDate != null && endDate != null) {
                    Log.d("SelectedDateRange", "Start Date: $startDate, End Date: $endDate")
                } else {
                    Log.d("SelectedDateRange", "No date range selected")
                }
            }

        }
    }

}



