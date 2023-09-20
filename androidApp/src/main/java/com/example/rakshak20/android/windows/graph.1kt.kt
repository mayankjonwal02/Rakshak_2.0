package com.example.rakshak20.android.windows

import android.widget.Toast
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import co.yml.charts.axis.AxisData
import co.yml.charts.common.model.Point
import co.yml.charts.ui.linechart.LineChart
import co.yml.charts.ui.linechart.model.*
import com.example.rakshak20.android.functions.MyBluetooth
import kotlin.math.roundToInt

@Composable
fun LineChartScreen(myBluetooth: MyBluetooth,type : String) {

    var graphcolor = Color.Blue
    var context = LocalContext.current

    val data = if (type == SCREEN1) {
        myBluetooth.ECGdata.collectAsState(emptyList()).value
    } else if(type == SCREEN2){
        myBluetooth.HeartRatedata.collectAsState(emptyList()).value
    }else if(type == SCREEN3){
        myBluetooth.SPO2data.collectAsState(emptyList()).value
    }
    else {
        myBluetooth.TEMPdata.collectAsState(emptyList()).value
    }






    val last20Data = data.takeLast(20) // Only take the last 20 data points
    val steps = data.size

    if (steps > 5) {
        val xAxisData = AxisData.Builder()
            .axisStepSize(30.dp) // Adjust the step size for the x-axis based on the last 20 data points
            .backgroundColor(Color.Transparent)
            .steps(steps - 1)
            .labelData { i -> i.toString() }
            .labelAndAxisLinePadding(15.dp)
            .axisLineColor(graphcolor)
            .axisLabelColor(graphcolor)
            .build()

        val maxDataValue = if (type == SCREEN1) {
             450

        } else if(type == SCREEN2){
            170
        }else if(type == SCREEN3){
           100
        }
        else {
             50
        }
        val yAxisData = AxisData.Builder()
            .steps(10)  // Fixed number of steps for the Y-axis
            .axisStepSize(30.dp)  // Adjust the step size for the Y-axis
            .backgroundColor(Color.Transparent)
            .labelAndAxisLinePadding(20.dp)
            .axisLineColor(graphcolor)
            .axisLabelColor(graphcolor)
            .labelData { i ->
                val stepValue = maxDataValue / 10
                (i * stepValue).toString()  // Calculate labels based on data range
            }.build()

        val lineChartData = LineChartData(
            linePlotData = LinePlotData(
                lines = listOf(
                    Line(
                        dataPoints = try{ data }catch (e:Exception){
                            listOf<Point>()
                            Toast.makeText(context,e.message.toString(),Toast.LENGTH_SHORT).show()
                        } as List<Point>,
                        LineStyle(
                            color = graphcolor,
                            lineType = LineType.SmoothCurve(isDotted = false)
                        ),
                        IntersectionPoint(color = graphcolor),
                        SelectionHighlightPoint(color = graphcolor),
                        ShadowUnderLine(
                            alpha = 0.5f,
                            brush = Brush.verticalGradient(
                                colors = listOf(graphcolor, Color.Transparent)
                            )
                        ),
                        SelectionHighlightPopUp(
                            backgroundColor = Color.Blue,
                            paddingBetweenPopUpAndPoint = 5.dp,
                            labelColor = Color.White
                        )
                    )
                ),
            ),
            xAxisData = xAxisData,
            yAxisData = yAxisData,
            gridLines = GridLines(Color.Gray),
            backgroundColor = Color.White
        )

        LineChart(
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp),
            lineChartData = lineChartData
        )
//        Text(text = data.toString())
    }
}
