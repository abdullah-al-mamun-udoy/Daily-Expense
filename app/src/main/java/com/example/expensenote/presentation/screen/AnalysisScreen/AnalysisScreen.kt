package com.example.expensenote.presentation.screen.AnalysisScreen

import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material.Colors
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center

import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asAndroidPath
import androidx.compose.ui.graphics.asComposePath
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalDensity

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.expensenote.database.entities.ExpenseItemEntity
import com.example.expensenote.ui.theme.appColor
import com.example.expensenote.viewmodel.ExpenseItemViewModel
import kotlinx.coroutines.delay
import kotlin.random.Random


@Composable
fun AnalysisScreen(navHost: NavHostController, viewModel: ExpenseItemViewModel = hiltViewModel()) {
    Text(text = "It will available soon")


    val expenseDataListState = viewModel.readAllExpenseList()?.collectAsState(initial = emptyList())

    val list = listOf(
        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        ),        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        ),        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        ),        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        ),        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        ),        ExpenseItemEntity(
            id = 0,
            expenseName = "",
            expenseAmount = "0",
            date = "",
            expenseDescription = ""
        )
    )

    Log.d("TAG", "AnalysisScreen: ${list.size}")

    // Get the value from the state object
    val expenseDataList = expenseDataListState?.value ?: list
    // Provide a default value
    // if the state is null
//    Log.d("TAG", "AnalysisScreen: ${expenseDataList.size}")

    var isEmpty = remember {
        mutableStateOf(false)
    }

    val dateList = mutableListOf<String>()
    val amountList = mutableListOf<Float>()

    LaunchedEffect(key1 = Unit) {
        delay(300).apply {
            isEmpty.value = true
        }
    }

    for (expenseItem in expenseDataList) {
        val date = expenseItem.date
        dateList.add(date)
        val amount = expenseItem.expenseAmount.toFloat()
        amountList.add(amount)

    }

//    val graphAppearance = GraphAppearance(
//        Color.White,
//        MaterialTheme.colors.primary,
//        1f,
//        true,
//        Color.Green,
//        false,
//        MaterialTheme.colors.secondary,
//        MaterialTheme.colors.background
//    )
    val yStep = 100
    val random = Random
    /* to test with random points */
    val points = (0..15).map {
        var num = random.nextInt(350)
        if (num <= 50)
            num += 100
        num.toFloat()
    }
    /* to test with fixed points */
//                val points = listOf(150f,100f,250f,200f,330f,300f,90f,120f,285f,199f),
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.DarkGray)
    ) {
        if (isEmpty.value)
            Graph(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(500.dp),
                xValues = (0..15).map { it + 1 },

                yValues = (0..6).map { (it + 1) * yStep },
                points = amountList,
                paddingSpace = 16.dp,
                verticalStep = yStep,
            )
    }

}


@Composable
fun Graph(
    modifier: Modifier,
    xValues: List<Int>,
    yValues: List<Int>,
    points: MutableList<Float>,
    paddingSpace: Dp,
    verticalStep: Int
) {
    val controlPoints1 = mutableListOf<PointF>()
    val controlPoints2 = mutableListOf<PointF>()
    val coordinates = mutableListOf<PointF>()
    val density = LocalDensity.current
    val textPaint = remember(density) {
        Paint().apply {
            color = Color.Red.toArgb()
            textAlign = Paint.Align.CENTER
            textSize = density.run { 12.sp.toPx() }
        }
    }
    Box(
        modifier = modifier
            .background(Color.White)
            .padding(horizontal = 8.dp, vertical = 12.dp),
        contentAlignment = Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxWidth()
                .height(460.dp),
        ) {
            val xAxisSpace = (size.width - paddingSpace.toPx()) / xValues.size
            val yAxisSpace = size.height / yValues.size
            /** placing x axis points */
            for (i in xValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${xValues[i]}",
                    xAxisSpace * (i + 1),
                    size.height - 30,
                    textPaint
                )
            }
            /** placing y axis points */
            for (i in yValues.indices) {
                drawContext.canvas.nativeCanvas.drawText(
                    "${yValues[i]}",
                    paddingSpace.toPx() / 2f,
                    size.height - yAxisSpace * (i + 1),
                    textPaint
                )
            }
            /** placing our x axis points */
            for (i in points.indices) {
                val x1 = xAxisSpace * xValues[i]
                val y1 = size.height - (yAxisSpace * (points[i] / verticalStep.toFloat()))
                coordinates.add(PointF(x1, y1))
                /** drawing circles to indicate all the points */
                if (true) {
                    drawCircle(
                        color = appColor,
                        radius = 10f,
                        center = Offset(x1, y1)
                    )
                }
            }
            /** calculating the connection points */
            for (i in 1 until coordinates.size) {
                controlPoints1.add(
                    PointF(
                        (coordinates[i].x + coordinates[i - 1].x) / 2,
                        coordinates[i - 1].y
                    )
                )
                controlPoints2.add(
                    PointF(
                        (coordinates[i].x + coordinates[i - 1].x) / 2,
                        coordinates[i].y
                    )
                )
            }
            /** drawing the path */
            val stroke = Path().apply {
                reset()
                moveTo(coordinates.first().x, coordinates.first().y)
                for (i in 0 until coordinates.size - 1) {
                    cubicTo(
                        controlPoints1[i].x, controlPoints1[i].y,
                        controlPoints2[i].x, controlPoints2[i].y,
                        coordinates[i + 1].x, coordinates[i + 1].y
                    )
                }
            }

            /** filling the area under the path */
            val fillPath = android.graphics.Path(stroke.asAndroidPath())
                .asComposePath()
                .apply {
                    lineTo(xAxisSpace * xValues.last(), size.height - yAxisSpace)
                    lineTo(xAxisSpace, size.height - yAxisSpace)
                    close()
                }
            if (true) {
                drawPath(
                    fillPath,
                    brush = Brush.verticalGradient(
                        listOf(
                            Color.Red,
                            Color.Transparent,
                        ),
                        endY = size.height - yAxisSpace
                    ),
                )
            }
            drawPath(
                stroke,
                color = Color.Blue,
                style = Stroke(
                    width = 2F,
                    cap = StrokeCap.Round
                )
            )
        }
    }
}
