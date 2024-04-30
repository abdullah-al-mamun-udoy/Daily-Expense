package com.example.expensenote.presentation.screen.main

import ExpenseDialog
import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.shrinkHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.ExtendedFloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensenote.constant.Constant
import com.example.expensenote.navigation.BottomBarNavigation
import com.example.expensenote.presentation.screen.setting.SettingViewmodel
import com.example.expensenote.ui.composable.BackgroundImage
import com.example.expensenote.ui.composable.ExpenseTemplate
import com.example.expensenote.ui.theme.appColor


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun MainScreen(navHostController: NavHostController) {

    val scaffoldState = rememberScaffoldState()
    var showDialog by remember { mutableStateOf(false) }

    BackgroundImage()
    Scaffold(
        bottomBar = {
            StandardBottomAppBar(
                navController = navHostController,
            )
        },
        scaffoldState = scaffoldState,
        backgroundColor = Color.Transparent,
//        floatingActionButton = {
//            ActionButton(onClick = { showDialog = true })
//        }

    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {

//            LazyColumn(
//                modifier = Modifier
//                    .padding(top = 160.dp) // Adjust top padding to position LazyColumn below floating action button
//                    .fillMaxSize()
//            ) {
//                repeat(10) {
//                    item {
//                        ExpenseTemplate()
//                        Spacer(modifier = Modifier.padding(bottom = 8.dp))
//                    }
//                }
//            }
//            Column(
//                modifier = Modifier
//                    .align(Alignment.BottomEnd)
//                    .padding(end = 10.dp)
//            ) {
//
//
//
//                ActionButton(onClick = { showDialog = true })
//
//                if (showDialog) {
//                    YourScreenContent(onDismiss = { showDialog = false })
//                }
//            }

            BottomBarNavigation(navHostController = navHostController)
        }
    }
}

//@Composable
//fun ActionButton(onClick: () -> Unit) {
//    ExtendedFloatingActionButton(
//        onClick = onClick,
//        icon = { Icon(Icons.Filled.Edit, "Extended floating action button.") },
//        text = { Text(text = "Add Expense") },
//        backgroundColor = Color.Red.copy(alpha = 0.6f),
//        contentColor = Color.White,
//    )
//    Spacer(modifier = Modifier.padding(8.dp))
//}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun YourScreenContent(onDismiss: () -> Unit) {
//    val visible by remember { mutableStateOf(true) }
//    AnimatedVisibility(
//        visible,
//        enter = expandHorizontally(animationSpec = tween(durationMillis = 300)),
//        exit = shrinkHorizontally(animationSpec = tween(durationMillis = 300))
//    ){
//        Column(
//            modifier = Modifier
//                .padding(horizontal = 16.dp)
//                .wrapContentSize(),
//            verticalArrangement = Arrangement.Center,
//            horizontalAlignment = Alignment.CenterHorizontally
//        ) {
//            ExpenseDialog(onDismiss)
//        }
//    }
//
//}

