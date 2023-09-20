package com.example.rakshak20.android.windows

import android.app.Application
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send

import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.rakshak20.android.API.ApiViewmodel
import com.example.rakshak20.android.API.medicaldata
import kotlinx.coroutines.CoroutineScope
//import com.example.rakshak20.android.database.DatabaseItems
//import com.example.rakshak20.android.database.databaseviewmodel

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SubmitDataForm()
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SubmitDataForm() {
    var context = LocalContext.current
//    val myviewmodal = databaseviewmodel(context.applicationContext as Application)
    var patientId by remember { mutableStateOf("") }
    var ecg by remember { mutableStateOf("") }
    var heartRate by remember { mutableStateOf("") }
    var spo2 by remember { mutableStateOf("") }
    var temperature by remember { mutableStateOf("") }
    var timestamp by remember { mutableStateOf("") }

    val keyboardController = LocalSoftwareKeyboardController.current
    val view = LocalView.current

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        TextFieldWithLabel("Patient ID", patientId) { patientId = it }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel("ECG", ecg) { ecg = it }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel("Heart Rate", heartRate) { heartRate = it }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel("SpO2", spo2) { spo2 = it }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel("Temperature", temperature) { temperature = it }
        Spacer(modifier = Modifier.height(16.dp))
        TextFieldWithLabel("Timestamp", timestamp) { timestamp = it }

        Spacer(modifier = Modifier.height(32.dp))

        var apiViewmodel = ApiViewmodel(context)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            IconButton(
                onClick = {

                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            var responce = apiViewmodel.senddataByAPI(medicaldata(patientid = patientId , heartrate = heartRate.toFloat() , spo2 = spo2.toFloat(), ecg = ecg.toFloat() , temperature = temperature.toFloat() , timestamp = LocalDateTime.now().toString()))
                            withContext(Dispatchers.Main){
                                Toast.makeText(context, responce.toString(), Toast.LENGTH_SHORT)
                                    .show()

                            }

                        }
                        catch (e:Exception)
                        {
                            withContext(Dispatchers.Main){
                                Log.e("TAG1",e.message.toString())
                                Toast.makeText(context, e.message.toString(), Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }

//                    myviewmodal.adddata(DatabaseItems(patientid = patientId ,
//                        spo2 = spo2.toFloat(),
//                        ecg = ecg.toFloat(),
//                        heartrate = heartRate.toFloat(),
//                        timestamp = timestamp.toString(),
//                    temperature = temperature.toFloat()))
                }
            ) {
                Icon(imageVector = Icons.Default.Send, contentDescription = null)
            }
        }
//        Text(text = myviewmodal.readalldata.observeAsState(listOf()).value.toString())
    }
}

@Composable
fun TextFieldWithLabel(
    label: String,
    value: String,
    onValueChange: (String) -> Unit
) {
    Column {
        Text(text = label)
        TextField(
            value = value,
            onValueChange = onValueChange,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions(
                onDone = {

                }
            ),
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.Transparent)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSubmitDataForm() {
    MaterialTheme {
        SubmitDataForm()
    }
}
