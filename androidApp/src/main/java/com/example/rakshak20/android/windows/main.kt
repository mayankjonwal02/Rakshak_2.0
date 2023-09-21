package com.example.rakshak20.android.windows

import DBHandler
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.rakshak20.android.API.ApiViewmodel
import com.example.rakshak20.android.API.medicaldata
import com.example.rakshak20.android.functions.getSharedPreferences
import com.example.rakshak20.android.navigation.navgraph1
import com.example.rakshak20.android.navigation.screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.IOException

@Composable
fun mainwindow(

    navHostController: NavHostController,
    context: Context
) {

    var context = LocalContext.current
    var navcontroller = rememberNavController()
    var scaffoldstate = rememberScaffoldState()
    var coroutinescope = rememberCoroutineScope()
    Scaffold(
        modifier = androidx.compose.ui.Modifier.fillMaxSize(),
        scaffoldState = scaffoldstate,
        topBar = {
                 TopAppBar(title ={},
                     elevation = 0.dp,
                 backgroundColor = Color.Transparent,
                     contentColor = Color.Gray,
                     navigationIcon = {
                         IconButton(onClick = { coroutinescope.launch { scaffoldstate.drawerState.open() }}) {
                             androidx.compose.material.Icon(imageVector = Icons.Default.Menu, contentDescription = "")
                         }
                     }
                 )
        },
        drawerShape = RoundedCornerShape(20.dp),
        drawerContent = {
//            Text(text = "Rakshak", color = Color.Blue, fontFamily = FontFamily.Default, fontStyle = FontStyle.Normal, fontWeight = FontWeight.ExtraBold,fontSize = 50.sp, textAlign = TextAlign.Center, modifier = androidx.compose.ui.Modifier.fillMaxWidth())
            mynavdrawer(navHostController,navcontroller)
        },
        drawerBackgroundColor = Color.White,
        backgroundColor = Color.White

    ) {
        navgraph1(oldnavcon = navHostController, navHostController = navcontroller, context = context, start = screen.connection.route)
    }
}


@Composable
fun mynavdrawer(navcontroller: NavHostController, navcontroller1: NavHostController) {

//    var currentPage by remember { mutableStateOf(screen.connection.route) }
    var context = LocalContext.current
    var sp = remember {
        getSharedPreferences(context)
    }

    var apiViewmodel  = ApiViewmodel(context)
    var dbHandler = remember {
        DBHandler(context)
    }

    Column(modifier = androidx.compose.ui.Modifier
        .fillMaxWidth()
        .wrapContentHeight()
        .background(
            Color.Transparent
        )) {
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
        Text(text = "User -> " + sp?.getString("patientid","").toString(), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, fontStyle = FontStyle.Normal, color = Color.Black ,modifier = Modifier
            .fillMaxWidth()
            .background(Color.Transparent)
            .padding(10.dp), textAlign = TextAlign.Center)
        Spacer(modifier = androidx.compose.ui.Modifier.height(20.dp))
//        Row(modifier = androidx.compose.ui.Modifier
//            .padding(10.dp)
//            .fillMaxWidth()
////            .height(10.dp)
//            .clickable { navcontroller.navigate(screen.connection.route) }
//
//            .background(Color.Transparent)
//            .padding(10.dp)
//            ,
//        ) {
//
//            Icon(imageVector = Icons.Filled.AutoGraph, contentDescription = "")
//            Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
//            Text(text = "Visualisation", modifier = androidx.compose.ui.Modifier.weight(1f))
//
//        }
        Spacer(modifier = androidx.compose.ui.Modifier.height(6.dp))
    }
    Row(
        modifier = androidx.compose.ui.Modifier
            .padding(10.dp)
            .fillMaxWidth()
//            .height(10.dp)
            .clickable {
                CoroutineScope(Dispatchers.IO).launch {
                    var response = apiViewmodel.getconnection()
                    withContext(Dispatchers.Main) {
                        Toast
                            .makeText(context, response, Toast.LENGTH_SHORT)
                            .show()
                    }
                    if (response.equals("Connection Successful")) {
                        var data = dbHandler.getAllPatientData()

                        CoroutineScope(Dispatchers.Main).launch {
                            for (i in data) {
                                try {

                                    var response = apiViewmodel.senddataByAPI(
                                        medicaldata(
                                            patientid = i.patientId,
                                            spo2 = i.spo2,
                                            heartrate = i.heartRate,
                                            ecg = i.ecg,
                                            temperature = i.temperature,
                                            timestamp = i.timestamp
                                        )
                                    )

                                } catch (e: IOException) {
                                    withContext(Dispatchers.Main) {
                                        Toast
                                            .makeText(
                                                context,
                                                e.message.toString(),
                                                Toast.LENGTH_SHORT
                                            )
                                            .show()
                                    }
                                    Log.e("TAG1", e.message.toString())
                                }
                            }
                            dbHandler.deleteAllPatientData()
                        }
                    } else {
                        withContext(Dispatchers.Main) {
                            Toast
                                .makeText(context, "Connection Failed", Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }

            }

            .background(Color.Transparent)
            .padding(10.dp),
    ) {

        Icon(imageVector = Icons.Filled.Share, contentDescription = "")
        Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
        Text(text = "Share Data", modifier = androidx.compose.ui.Modifier.weight(1f))

    }
    Spacer(modifier = androidx.compose.ui.Modifier.height(6.dp))
    Row(
        modifier = androidx.compose.ui.Modifier
            .padding(10.dp)
            .fillMaxWidth()
//            .height(10.dp)
            .clickable {

                sp
                    ?.edit()
                    ?.putString("patientid", "")
                    ?.apply()
                sp
                    ?.edit()
                    ?.putString("password", "")
                    ?.apply()
                navcontroller.navigate(screen.ipscreen.route)


            }

            .background(Color.Transparent)
            .padding(10.dp),
    ) {

        Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
        Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
        Text(text = "Logout", modifier = androidx.compose.ui.Modifier.weight(1f))

    }
    Spacer(modifier = androidx.compose.ui.Modifier.height(6.dp))
}