import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.rakshak20.android.API.ApiViewmodel
import com.example.rakshak20.android.API.medicaldata
import kotlinx.coroutines.*
import java.io.IOException

@Composable
fun demopage4() {
    var context = LocalContext.current
    var apiViewmodel = ApiViewmodel(context)
    Box(modifier = Modifier.fillMaxSize())
    {

        val dbHandler = remember {
            DBHandler(context)
        }
        var data by remember {
            mutableStateOf(dbHandler.getAllPatientData().toString())
        }
        Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
            Button(onClick = {
                dbHandler.addPatientData(
                    "patient1",
                    123.2F,
                    123.2F,
                    123.2F,
                    100F
                )
                data = dbHandler.getAllPatientData().toString()
                Toast.makeText(context, "Patient data added to Database", Toast.LENGTH_SHORT).show()
            }) {
                Text(text = "Add Patient Data to Database", color = Color.White)
            }

            Text(text = data, modifier = Modifier.clickable{ data = dbHandler.getAllPatientData().toString()})


            Button(onClick = { dbHandler.deleteAllPatientData()
                data = dbHandler.getAllPatientData().toString()}) {
                Text(text = "delete")
            }

            Button(onClick = {
                CoroutineScope(Dispatchers.Main).launch {
                    val response = apiViewmodel.senddataByAPI(
                        medicaldata(
                            patientid = "hello",
                            spo2 = 123.4F,
                            heartrate = 12.4F,
                            ecg = 132.4F,
                            temperature = 12.3F,
                            timestamp = "sacaicn"
                        )
                    )

                    Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                    Log.e("TAG1",response)
                }
            }) {
                Text(text = "send")
            }

        }
    }




}



suspend fun makeApiCall(context: Context ,apiViewmodel : ApiViewmodel) {
    var response : String = ""
    try {
        // Make the network call
        try{
             response = apiViewmodel.senddataByAPI(
                medicaldata(
                    patientid = "hello",
                    spo2 = 123.4F,
                    heartrate = 12.4F,
                    ecg = 132.4F,
                    temperature = 12.3F,
                    timestamp = "sacaicn"
                )
            )
        }
        catch (e:IOException)
        {
            Toast.makeText(context, "IOException: ${e.message}", Toast.LENGTH_LONG).show()
        }

        // Check if the response is successful
        if (response.isNotBlank()) {
            // Handle the response accordingly
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Data sent successfully", Toast.LENGTH_LONG).show()
            }
        } else {
            // Handle unsuccessful response
            val errorBody = response.toString()
            Log.e("TAG1", "Error: ${response.toString()}, Message: $errorBody")
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Failed to send data. Error: ${response.toString()}", Toast.LENGTH_LONG).show()
            }
        }
    } catch (e: IOException) {
        // Handle IO exception
        Log.e("TAG1", "IOException: ${e.message}", e)
        withContext(Dispatchers.Main) {
            Toast.makeText(context, "IOException: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }
}


@Composable
fun MedicalDataForm(context: Context,apiViewModel: ApiViewmodel) {
    var patientId by remember { mutableStateOf("ninja") }
    var heartRate by remember { mutableStateOf("123") }
    var spo2 by remember { mutableStateOf("123") }
    var ecg by remember { mutableStateOf("123") }
    var temperature by remember { mutableStateOf("123") }
    var timestamp by remember { mutableStateOf("mytime") }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TextField(
            value = patientId,
            onValueChange = { patientId = it },
            label = { Text("Patient ID") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = heartRate,
            onValueChange = { heartRate = it },
            label = { Text("Heart Rate") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = spo2,
            onValueChange = { spo2 = it },
            label = { Text("Spo2") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = ecg,
            onValueChange = { ecg = it },
            label = { Text("ECG") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = temperature,
            onValueChange = { temperature = it },
            label = { Text("Temperature") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            value = timestamp,
            onValueChange = { timestamp = it },
            label = { Text("Timestamp") }
        )

        Spacer(modifier = Modifier.height(32.dp))

        Button(onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var responce = apiViewModel.senddataByAPI(medicaldata(patientid = patientId, spo2 = spo2.toFloat(), heartrate = heartRate.toFloat(), ecg = ecg.toFloat(), temperature = temperature.toFloat(), timestamp = timestamp))
                    GlobalScope.launch(Dispatchers.Main) {
//                        Toast.makeText(context,responce.toString(),Toast.LENGTH_LONG).show()
                    }
                }

                catch (e:Exception)
                {
                    GlobalScope.launch(Dispatchers.Main) {
//                        Toast.makeText(context,e.message.toString(),Toast.LENGTH_LONG).show()
                    }
                }
            }
        }) {
            Text("Submit")
        }
    }
}