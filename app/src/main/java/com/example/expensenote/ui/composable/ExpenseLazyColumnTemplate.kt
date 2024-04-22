package com.example.expensenote.ui.composable

import MoreIconPopUpMenu
import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandIn
import androidx.compose.animation.shrinkOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.expensenote.R
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.ui.theme.customFontFamily


@SuppressLint("CoroutineCreationDuringComposition")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ExpenseTemplate(
    index:Int,
    initialExpenseName: String,
    initialExpenseAmount: String,
    initialDateTime:String,
    initialExpenseDescription: String?,
    onClick: () -> Unit = {}
) {
    var visible by remember {
        mutableStateOf(false)
    }
    var corner by remember {
        mutableStateOf(10)
    }

    var showMoreIcon by remember {
        mutableStateOf(false)
    }


    Log.d("visible", "ExpenseTemplate: $visible ")
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape = RoundedCornerShape(corner))
            .background(appColor.copy(alpha = .8f))
            .wrapContentHeight()
            .clickable { visible = !visible }

    ) {
        corner = if (visible) 5 else 10

        val fadedTextColor = Color.DarkGray.copy(alpha = 0.8f)
        Column(
            modifier = Modifier
                .padding(horizontal = 12.dp, vertical = 2.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Expense Log",
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                fontSize = 18.sp,
                fontFamily = customFontFamily,
                color = Color.White
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 2.dp)
            ) {
                Column(
                    Modifier
                        .weight(.55f)
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "Expense Name :",
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 2.dp),
                        color = Color.White
                    )
                    Text(
                        text = "Expense Amount :",
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 2.dp),
                        color = Color.White

                    )

                    if (visible) {
                        Text(
                            text = "Date & Time :",
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(bottom = 2.dp),
                            color = Color.White

                        )
                    }
                }


                Column(
                    modifier = Modifier
                        .weight(.90f)
                        .padding(vertical = 2.dp)
                ) {
                    Text(
                        text = "  $initialExpenseName",
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 2.dp),
                        color = fadedTextColor
                    )
                    Text(
                        text = "  $initialExpenseAmount",
                        fontFamily = FontFamily.Serif,
                        modifier = Modifier.padding(bottom = 2.dp),
                        color = fadedTextColor
                    )
                    if (visible) {
                        Text(
                            text = "  $initialDateTime",
                            fontFamily = FontFamily.Serif,
                            modifier = Modifier.padding(bottom = 2.dp),
                            color = fadedTextColor
                        )
                    }
                }

            }
            AnimatedVisibility(
                visible,
                enter = expandIn(animationSpec = tween(durationMillis = 1800)),
                exit = shrinkOut(animationSpec = tween(durationMillis = 1800))
            ) {
                Column() {
                    Text(
                        text = "Expense Description",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth(),
                        fontFamily = FontFamily.Monospace,
                        color = Color.White
                    )
                    Text(
                        text = initialExpenseDescription!!,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 8.dp),
                        fontFamily = FontFamily.Serif,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis,
                        color = fadedTextColor
                    )


                }

            }

        }
        Image(
            painter = painterResource(id = R.drawable.ic_more), contentDescription = "more",
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(end = 4.dp, top = 6.dp)
                .clickable {
                    showMoreIcon = true
                }

        )
        if (showMoreIcon){
            Box(modifier = Modifier.align(Alignment.TopEnd)) {
                MoreIconPopUpMenu(
                    expenseItemEntity = ExpenseItemEntity(
                        id = index,
                        expenseName = initialExpenseName,
                        expenseAmount = initialExpenseAmount,
                        date = initialDateTime,
                        expenseDescription = initialExpenseDescription
                    ),
                    onDismiss = { showMoreIcon = false },



                )

            }

        }

    }

}

//@RequiresApi(Build.VERSION_CODES.O)
//@Composable
//fun ExpenseItem(item:ExpenseItemEntity){
//    ExpenseTemplate(
//        initialExpenseName = item.expenseName,
//        initialExpenseAmount = item.expenseAmount,
//        initialDateTime = item.date,
//        initialExpenseDescription = item.expenseDescription ?: ""
//    )
//}


