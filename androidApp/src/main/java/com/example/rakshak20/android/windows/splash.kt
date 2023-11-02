package com.example.rakshak20.android.windows

import android.content.Context
import androidx.compose.animation.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Medication
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import co.yml.charts.common.extensions.isNotNull
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.functions.getSharedPreferences
import com.example.rakshak20.android.navigation.screen


@Composable
fun splash(navHostController: NavHostController, context: Context) {

    getpermissions(context)  //    requesting permissions
    var iconsize = remember {
        androidx.compose.animation.core.Animatable(0f)
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(color = Color.White), contentAlignment = Alignment.Center)
    {
        Icon(imageVector = Icons.Filled.Medication, contentDescription = "", tint = Color.Blue , modifier = Modifier
            .background(
                Color.Transparent
            )
            .size(iconsize.value.dp))
    }

    var sp = remember {

    getSharedPreferences(context)}



    LaunchedEffect(Unit)
    {
        iconsize.animateTo(250f , animationSpec = tween(
            durationMillis = 1000,
            delayMillis = 30,
            easing = LinearOutSlowInEasing
        ))
//        sp?.getString("ipaddress","0.0.0.0") == "0.0.0.0" ||
        if(sp?.getString("patientid","") == "" || sp?.getString("password","") == "")
        {
            navHostController.navigate(screen.option.route)
        }
        else
        {
            navHostController.navigate(screen.main.route)
        }


    }
    
}