package com.example.expensenote.presentation.screen.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.expensenote.R
import com.example.expensenote.navigation.Screen
import com.example.expensenote.ui.theme.appBackground
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navHost: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .background(appBackground),
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(painterResource(id = R.drawable.ic_logo), contentDescription = "null")

        LaunchedEffect(Unit) {
            delay(2000)

            navHost.navigate(Screen.OnBoardingScreen.route) {
                popUpTo(Screen.SplashScreen.route) {
                    inclusive = true
                }
            }

        }


    }


}