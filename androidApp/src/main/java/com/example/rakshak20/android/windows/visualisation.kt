package com.example.rakshak20.android.windows

//import LineGraph
import LineChartComposable
import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.rakshak20.android.functions.MyBluetooth
//io.jetchart.line.Point
var SCREEN1 = "ecg"
var SCREEN2 = "heartrate"
var SCREEN3 = "spo2"
var SCREEN4 = "temp"  // spo2, temp
@Composable
fun visualisation(context: Context, mybluetooth: MyBluetooth) {

//    code for screen

    var screen by remember {
        mutableStateOf(SCREEN1)
    }

//    code for graph visualisation

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center
    )
    {
        var data = if (screen == SCREEN1) {
            mybluetooth.ECGdata.collectAsState(emptyList()).value
        } else if (screen == SCREEN2) {
            mybluetooth.HeartRatedata.collectAsState(emptyList()).value
        } else if (screen == SCREEN3) {
            mybluetooth.SPO2data.collectAsState(emptyList()).value
        } else {
            mybluetooth.TEMPdata.collectAsState(emptyList()).value
        }
//        var data20 = data.takeLast(20)
        var datalast = if(data.size > 5) {data.last().value.toString()} else {""}
        Column(verticalArrangement = Arrangement.Top){
            if(screen == SCREEN1){
                Text(
                    text = "${screen.toUpperCase()}  ${datalast}",
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent),
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Default,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Normal
                )
            }

            Spacer(modifier = Modifier.height(50.dp))
            Crossfade(targetState = screen) { it ->
                when (it) {
                    SCREEN1 -> LineChartComposable(myBluetooth = mybluetooth, type = SCREEN1)
                    SCREEN2 -> LineChartComposable(myBluetooth = mybluetooth, type = SCREEN2)
                    SCREEN3 -> LineChartComposable(myBluetooth = mybluetooth, type = SCREEN3)
                    SCREEN4 -> LineChartComposable(myBluetooth = mybluetooth, type = SCREEN4)
                }
            }
        }
    }

//    code for bottom navigation bar

        Box(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent),
        contentAlignment = Alignment.BottomCenter)
    {
        Card(
            modifier = Modifier
                .padding(20.dp)
                .fillMaxWidth()
                .background(Color.Transparent),
            elevation = 10.dp,
            shape = RoundedCornerShape(20.dp),

        ){
            BottomNavigation() {
                BottomNavigationItem(
                    selected = screen == SCREEN1,
                    onClick = {
                        if (screen != SCREEN1) {
                            screen = SCREEN1
                        }
                    },
                    label = { Text(text = "ECG") },
                    icon = {},
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    modifier = Modifier.background(if(screen == SCREEN1) Color.Blue else Color.LightGray)
                )
                BottomNavigationItem(
                    selected = screen == SCREEN2,
                    onClick = {
                        if (screen != SCREEN2) {
                            screen = SCREEN2
                        }
                    },
                    label = { Text(text = "Heart Rate") },
                    icon = {},
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,


                    modifier = Modifier.background(
                       if(screen == SCREEN2) Color.Blue else Color.LightGray
                    )
                )
                BottomNavigationItem(
                    selected = screen == SCREEN3,
                    onClick = {
                        if (screen != SCREEN3) {
                            screen = SCREEN3
                        }
                    },
                    label = { Text(text = "SP02") },
                    icon = {},
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    modifier = Modifier.background(if(screen == SCREEN3) Color.Blue else Color.LightGray)
                )
                BottomNavigationItem(
                    selected = screen == SCREEN4,
                    onClick = {
                        if (screen != SCREEN4) {
                            screen = SCREEN4
                        }
                    },
                    label = { Text(text = "T/F") },
                    icon = {},
                    selectedContentColor = Color.White,
                    unselectedContentColor = Color.Gray,
                    modifier = Modifier.background(if(screen == SCREEN4) Color.Blue else Color.LightGray)
                )
            }
        }
    }
}