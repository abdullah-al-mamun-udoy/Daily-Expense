package com.example.expensenote.presentation.screen.setting

import android.widget.EditText
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.expensenote.R
import com.example.expensenote.ui.theme.appColor

@Composable
fun SettingScreen(navhost: NavHostController) {

    var name by remember { mutableStateOf("") }


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

                                }
                        )
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