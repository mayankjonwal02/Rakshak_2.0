package com.example.rakshak20.android.windows

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Card
import androidx.compose.material.OutlinedButton
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.ActivityCompat
import androidx.navigation.NavHostController
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.functions.getSharedPreferences

import com.example.rakshak20.android.navigation.screen
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.io.IOException

@Composable
fun connection(navHostController: NavHostController, context: Context, mybluetooth: MyBluetooth) {

    val navigated by mybluetooth.navigated.collectAsState()
    var deviceAddress : String = ""
    var sp = remember{
        getSharedPreferences(context)
    }
//    mybluetooth.connect()
//    try
//    {
//        if (mybluetooth.bluetoothAdapter.isEnabled) {
//            deviceAddress = sp?.getString("bluetoothAddress","").toString()
//            if(deviceAddress != "")
//            {
//                var device = mybluetooth.bluetoothAdapter.getRemoteDevice(deviceAddress)
//                mybluetooth.clientclass(device).start()
//            }
//
//        }
//    }
//    catch (e:IOException)
//    {
//        Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
//    }

    var intentlauncher =  rememberLauncherForActivityResult(contract = ActivityResultContracts.StartActivityForResult())
    {
            result ->

        if(result.resultCode  == Activity.RESULT_OK)
        {
            mybluetooth.fetchPairedDevices()
        }
        else
        {
            Toast.makeText(context,"Bluetooth Not Enabled",Toast.LENGTH_LONG).show()
        }
    }
    val status by mybluetooth.status.collectAsState()

    val mylist by mybluetooth.paireddevices.collectAsState(emptyList())
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White))
    {
        Column(modifier = Modifier
            .fillMaxSize()
            .background(Color.White), horizontalAlignment = Alignment.CenterHorizontally) {
            OutlinedButton(onClick = { fetchdevices(mybluetooth,context,intentlauncher) },
                colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Blue, contentColor = Color.White),
                modifier = Modifier.padding(65.dp)) {
                Text(text = "Scan Devices", fontSize = 20.sp)
                
            }
            if(mylist.size > 0){
                Card(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxWidth()
                        .height(300.dp),
                    backgroundColor = Color.Blue,
                    shape = RoundedCornerShape(20.dp)
                )
                {
                    LazyColumn(modifier = Modifier.padding(20.dp))
                    {
                        items(mylist)
                        { item ->
                            Card(modifier = Modifier
                                .clickable {
                                    if (item != null) {
                                        try {
//                                    sp?.edit()?.putString("bluetoothAddress",item.address)?.apply()
                                            mybluetooth
                                                .clientclass(item)
                                                .start()
                                        } catch (e: IOException) {
                                            Toast
                                                .makeText(
                                                    context,
                                                    e.message.toString(),
                                                    Toast.LENGTH_LONG
                                                )
                                                .show()
                                        }
                                    }
                                }
                                .padding(10.dp)
                                .fillMaxWidth()
                                .wrapContentHeight(),
                                backgroundColor = Color.White,
                                contentColor = Color.Blue,
                                shape = RoundedCornerShape(20.dp)) {
                                if (ActivityCompat.checkSelfPermission(
                                        context,
                                        Manifest.permission.BLUETOOTH_CONNECT
                                    ) != PackageManager.PERMISSION_GRANTED
                                ) {
                                    Toast.makeText(
                                        context,
                                        "Permissions not Granted",
                                        Toast.LENGTH_LONG
                                    ).show()
                                }
                                item?.name?.let {
                                    Text(
                                        text = it,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(5.dp),
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Text(
                text = "Status : ${status}",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp, bottom = 5.dp),
                textAlign = TextAlign.Center,
                color = Color.Black
            )

            if(status == "Connected"){

                OutlinedButton(
                    onClick = { navHostController.navigate(screen.visualise.route) },
                    colors = ButtonDefaults.outlinedButtonColors(
                        backgroundColor = Color.Blue,
                        contentColor = Color.White
                    ),
                    modifier = Modifier.padding(15.dp)
                ) {
                    Text(text = "Proceed", fontSize = 20.sp)

                }
            }
        }
    }
}



fun fetchdevices(
    mybluetooth: MyBluetooth,
    context: Context,
    intentlauncher: ManagedActivityResultLauncher<Intent, ActivityResult>
)
{

    fun handleintent()
    {
        var intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
        intentlauncher.launch(intent)
    }
    
    if(mybluetooth.bluetoothAdapter.isEnabled)
    {
        mybluetooth.fetchPairedDevices()
    }
    else
    {
        handleintent()
    }
}