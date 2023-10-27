package com.example.rakshak20.android.windows

import android.graphics.Point
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.round
import kotlin.math.roundToInt

import kotlin.random.Random

fun generateRandomData(): List<co.yml.charts.common.model.Point> {
    val random = Random.Default
    val data = mutableListOf<co.yml.charts.common.model.Point>()



    for (i in 1..10) {
        val randomDouble = random.nextDouble(0.0, 1.0)*10
        data.add(co.yml.charts.common.model.Point(i.toFloat(), randomDouble.toFloat()))
    }


    return data
}

//  code snippet for graph

//@Composable
//fun graph(data : List<co.yml.charts.common.model.Point>)
//{
//    var spacing = 100f
//    var graphcolor = Color.Blue
//    var transparentgraphcolor = remember {
//        graphcolor.copy(alpha = 0.5f)
//    }
//    var uppervalue = remember {
//        (data.maxOfOrNull { it.second }?.plus(1))?.roundToInt()?:0
//    }
//    var lowervalue = remember {
//        (data.minOfOrNull { it.second })?.roundToInt()?:0
//    }
//    var density = LocalDensity.current
//    var textpaint = remember(density) {
//
//        android.graphics.Paint().apply {
//            color = android.graphics.Color.BLACK
//            textAlign = android.graphics.Paint.Align.CENTER
//            textSize = density.run { 12.sp.toPx() }
//
//        }
//    }
//
//    Canvas(modifier = Modifier
//        .fillMaxWidth()
//        .height(300.dp)
//        .padding(20.dp) )
//    {
//        var spaceperhour = (size.width - spacing)/data.size
//        (data.indices step 2).forEach{ i ->
//            var hour = data[i].first
//            drawContext.canvas.nativeCanvas.apply {
//                drawText(
//                    hour.toString(),
//                    spacing + i*spaceperhour,
//                    size.height,
//                    textpaint
//                )
//            }
//        }
//
//        var pricestep = (uppervalue - lowervalue)/5f
//        (0 until data.size).forEach{ i ->
//            drawContext.canvas.nativeCanvas.apply {
//                drawText(
//                    round(lowervalue + pricestep*i).toString(),
//                    30f,
//                    size.height - spacing - i * size.height / data.size.toFloat(),
//                    textpaint
//                )
//            }
//
//        }
//
//        var strokepath = Path().apply {
//            var height = size.height
//            data.indices.forEach{ i ->
//                var info = data[i]
//                var ratio = (info.second - lowervalue) / (uppervalue - lowervalue)
//
//                var x1 = spacing + i*spaceperhour
//                var y1 = height - spacing - (ratio * height).toFloat()
//                if(i == 0) moveTo(x1, y1)
//                lineTo(x1,y1)
//            }
//        }
//
//        drawPath(
//            path =  strokepath,
//            color = graphcolor,
//            style = Stroke(
//                width = 2.dp.toPx(),
//                cap = StrokeCap.Round
//            )
//        )
//
//        var fillpath = android.graphics.Path(strokepath.asAndroidPath()).asComposePath().apply {
//            lineTo(size.width - spacing , size.height - spacing)
//            lineTo(spacing , size.height - spacing)
//            close()
//        }
//
//        drawPath(
//            path = fillpath,
//            brush = Brush.verticalGradient(
//                colors = listOf(
//                    transparentgraphcolor,
//                    Color.Transparent
//                ),
//                endY = size.height - spacing
//            )
//        )
//
//    }
//}