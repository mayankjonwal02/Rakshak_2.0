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
import com.example.rakshak20.android.navigation.bluetooth
import com.example.rakshak20.android.navigation.navgraph1
import com.example.rakshak20.android.navigation.screen
import kotlinx.coroutines.*
import java.io.IOException

@Composable
fun mainwindow(

    navHostController: NavHostController,
    context: Context
) {
    var current_screen = remember {
        mutableStateOf("")
    }

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
                         IconButton(onClick = { if(current_screen.value != "countdown-start"){ coroutinescope.launch { scaffoldstate.drawerState.open() } }}) {
                             androidx.compose.material.Icon(imageVector = Icons.Default.Menu, contentDescription = "" , tint =  if(current_screen.value != "countdown-start"){Color.Black} else { Color.LightGray})
                         }
                     }
                 )
        },
        drawerShape = RoundedCornerShape(20.dp),
        drawerContent = {
//            if(current_screen.value != "countdown-start"){
//            Text(text = "Rakshak", color = Color.Blue, fontFamily = FontFamily.Default, fontStyle = FontStyle.Normal, fontWeight = FontWeight.ExtraBold,fontSize = 50.sp, textAlign = TextAlign.Center, modifier = androidx.compose.ui.Modifier.fillMaxWidth())
                if (current_screen.value != "countdown-start") {
                    mynavdrawer(navHostController, navcontroller, current_screen)
                }
//            }
        },
        drawerBackgroundColor = Color.White,
        backgroundColor = Color.White,


    ) {
        navgraph1(oldnavcon = navHostController, navHostController = navcontroller, context = context, start = screen.connection.route , current_screen)
    }
}


@Composable
fun mynavdrawer(
    navcontroller: NavHostController,
    navcontroller1: NavHostController,
    current_screen: MutableState<String>
) {

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
//            .clickable { navcontroller1.navigate(screen.countdown.route) }
//
//            .background(Color.Transparent)
//            .padding(10.dp)
//            ,
//        ) {
//
//            Icon(imageVector = Icons.Filled.AutoGraph, contentDescription = "")
//            Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
//            Text(text = "Visualise Again", modifier = androidx.compose.ui.Modifier.weight(1f))
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
                var job1 = CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val connectionResponse = apiViewmodel.getconnection()

                        var job4 = GlobalScope.launch(Dispatchers.Main) {
                            Toast
                                .makeText(context, connectionResponse, Toast.LENGTH_SHORT)
                                .show()
                        }

                        job4.join()

                        if (connectionResponse == "Connection Successful") {
                            val data = dbHandler.getAllPatientData()


                            var job2 = CoroutineScope(Dispatchers.IO).launch {
                                for (i in data) {
                                    Log.e("TAG1",i.toString())
                                    try {
                                        val response = apiViewmodel.senddataByAPI(
                                            medicaldata(
                                                patientid = i.patientId,
                                                spo2 = i.spo2,
                                                heartrate = i.heartRate,
                                                ecg = i.ecg,
                                                temperature = i.temperature,
                                                timestamp = i.timestamp,
                                                Medical_Id = i.Medical_id
                                            )


                                        )
                                        Log.e("TAG1",response.toString())
                                        // Handle response if needed
                                    } catch (e: IOException) {
                                        var job3 = GlobalScope.launch(Dispatchers.Main) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    e.message.toString(),
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                        job3.join()
                                        Log.e("TAG1", e.message.toString())
                                    }
                                }
                                dbHandler.deleteAllPatientData()
                            }
                            job2.join()
                        } else {
                            withContext(Dispatchers.Main) {
                                Toast
                                    .makeText(context, "Connection Failed", Toast.LENGTH_SHORT)
                                    .show()
                            }


                        }
                    } catch (e: Exception) {
                        // Handle exceptions appropriately
                        Log.e("TAG", "Exception: ${e.message}", e)
                    }
                }


            }

            .background(Color.Transparent)
            .padding(10.dp),
    ) {

        Icon(imageVector = Icons.Filled.Share, contentDescription = "")
        Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
        Text(text = "Sync Data", modifier = androidx.compose.ui.Modifier.weight(1f), color = Color.Black)

    }
    Spacer(modifier = androidx.compose.ui.Modifier.height(6.dp))

        Row(
            modifier = androidx.compose.ui.Modifier
                .padding(10.dp)
                .fillMaxWidth()
//            .height(10.dp)
                .clickable {

                    if (current_screen.value != "countdown-start") {
                        sp
                            ?.edit()
                            ?.putString("patientid", "")
                            ?.apply()
                        sp
                            ?.edit()
                            ?.putString("medicalid", "")
                            ?.apply()
                        sp
                            ?.edit()
                            ?.putString("password", "")
                            ?.apply()

                        bluetooth?.socket?.close()

                        navcontroller.navigate(screen.option.route)
                    }


                }

                .background(Color.Transparent)
                .padding(10.dp),
        ) {

            Icon(imageVector = Icons.Filled.Logout, contentDescription = "")
            Spacer(modifier = androidx.compose.ui.Modifier.width(16.dp))
            Text(
                text = "Logout",
                modifier = androidx.compose.ui.Modifier.weight(1f),
                color =  if(current_screen.value != "countdown-start"){ Color.Black } else {Color.LightGray}
            )

        }
//    }
    Spacer(modifier = androidx.compose.ui.Modifier.height(6.dp))
}