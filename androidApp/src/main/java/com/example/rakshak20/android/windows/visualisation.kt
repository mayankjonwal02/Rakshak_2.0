package com.example.rakshak20.android.windows

//import LineGraph
import android.content.Context
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.rakshak20.android.functions.MyBluetooth

var SCREEN1 = "ecg"
var SCREEN2 = "heartrate"
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
        Crossfade(targetState = screen) {
            it ->
            when(it)
            {
                SCREEN1 -> LineChartScreen(myBluetooth = mybluetooth, type = SCREEN1)
                SCREEN2 -> LineChartScreen(myBluetooth = mybluetooth,type = SCREEN2)
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
            }
        }
    }
}