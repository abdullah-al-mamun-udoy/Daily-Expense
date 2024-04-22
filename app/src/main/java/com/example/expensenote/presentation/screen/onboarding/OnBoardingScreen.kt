package com.example.expensenote.presentation.screen.onboarding

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import com.example.expensenote.R
import com.example.expensenote.navigation.Graph
import com.example.expensenote.navigation.Screen
import com.example.expensenote.ui.theme.appBackground
import com.example.expensenote.util.CustomFont
import kotlinx.coroutines.delay


@Composable
fun OnBoardingScreen(navhost: NavHostController) {

    Column(
        Modifier
            .fillMaxSize()
            .background(appBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Box(modifier = Modifier.height(400.dp)) {
            val composition by rememberLottieComposition(spec = LottieCompositionSpec.RawRes(R.raw.splashscreen1))
            LottieAnimation(composition = composition, iterations = LottieConstants.IterateForever)
            Spacer(modifier = Modifier.padding(2.dp))

            var showText by remember { mutableStateOf(false) }

//            val showText: MutableState<Boolean> = mutableStateOf(true)

            LaunchedEffect(Unit) {
                delay(0) // Adjust delay time as needed
                showText = true
            }

            if (showText) {
                Text(
                    text = "Analyse your expense, and monitor wisely...",
                    fontSize = 16.sp,
                    fontFamily = CustomFont.poppinsRegular,
                    fontWeight = FontWeight.Normal,
                    modifier = Modifier
                        .padding(start = 40.dp, top = 350.dp)
                        .fillMaxWidth()
                )
            }

            LaunchedEffect(Unit){
                delay(500)
                navhost.navigate(Graph.BottomGraph.route){
                    popUpTo(Screen.OnBoardingScreen.route)
                }

            }
        }


    }

}