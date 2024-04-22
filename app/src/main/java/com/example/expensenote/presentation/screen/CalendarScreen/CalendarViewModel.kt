package com.example.expensenote.presentation.screen.CalendarScreen

import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModel
import java.time.LocalDate

class CalendarViewModel : ViewModel() {

    val selectedDateRange =
        mutableStateOf<Pair<LocalDate?, LocalDate?>>(null to null)



}