package com.example.expensenote.presentation.screen.setting

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.example.expensenote.R
import com.example.expensenote.ui.theme.appColor

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(navhost: NavHostController, viewmodel: SettingViewmodel = hiltViewModel()) {

    var name by remember { mutableStateOf("") }

    val coroutineScope = rememberCoroutineScope()
    val modalSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = false,
        confirmValueChange = { true }
    )

    val isModalVisible by viewmodel.isModalVisible.collectAsStateWithLifecycle()


    Box(modifier = Modifier.fillMaxSize()) {
        Column() {

            Row(
                modifier = Modifier.padding(start = 20.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "back_button", modifier = Modifier.size(24.dp)
                )
                Text(text = "Back", modifier = Modifier.padding(start = 4.dp), fontSize = 20.sp)
            }
            Row(
                modifier = Modifier.padding(start = 10.dp, top = 24.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box() {
                    Image(
                        painter = painterResource(id = R.drawable.ic_dp),
                        contentDescription = "dp",
                        modifier = Modifier
                            .size(72.dp)
                    )
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd) // Align the box to the bottom end (right bottom corner)
                            .padding(6.dp) // Add padding to create some space between the corner and the image
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_camera),
                            contentDescription = "dp",
                            modifier = Modifier
                                .size(20.dp)
                                .clickable {
                                    viewmodel.showModalSheet()
                                }
                        )
                        if (isModalVisible) {
                            ModalBottomSheet(
                                sheetState = modalSheetState,
                                onDismissRequest = { viewmodel.hideModalSheet() }) {

                                Column(modifier = Modifier.padding(vertical = 0.dp)) {
                                    Row(
                                        modifier = Modifier.padding(start = 20.dp, top = 0.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_dp),
                                            contentDescription = "dp",
                                            modifier = Modifier.size(24.dp)
                                        )
                                        Text(
                                            text = "See profile picture",
                                            modifier = Modifier.padding(start = 4.dp),
                                            fontSize = 20.sp
                                        )
                                    }
                                    Row(
                                        modifier = Modifier.padding(
                                            start = 20.dp,
                                            top = 16.dp,
                                            bottom = 24.dp
                                        ),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Image(
                                            painter = painterResource(id = R.drawable.ic_gallary),
                                            contentDescription = "dp",
                                            modifier = Modifier
                                                .size(24.dp)
                                                .clickable {

                                                }
                                        )
                                        Text(
                                            text = "Upload profile picture",
                                            modifier = Modifier.padding(start = 4.dp),
                                            fontSize = 20.sp
                                        )
                                    }

                                }

                            }
                        }
                    }

                }
                TextField(
                    value = name, // You can bind this to a variable if needed
                    onValueChange = { name = it },
                    placeholder = { Text("Write Your Name", fontSize = 20.sp) },
                    maxLines = 1,
                    colors = TextFieldDefaults.textFieldColors(
                        backgroundColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        cursorColor = appColor
                    )
                )
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_email),
                    contentDescription = "Email", modifier = Modifier.size(24.dp)
                )
                Text(text = "Email us", modifier = Modifier.padding(start = 4.dp), fontSize = 20.sp)
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_send),
                    contentDescription = "send", modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Crash Analytics and Report",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 20.sp
                )
            }
            Row(
                modifier = Modifier.padding(start = 20.dp, top = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_clear),
                    contentDescription = "clear", modifier = Modifier.size(24.dp)
                )
                Text(
                    text = "Clear Storage",
                    modifier = Modifier.padding(start = 4.dp),
                    fontSize = 20.sp
                )
            }
        }


    }
}

