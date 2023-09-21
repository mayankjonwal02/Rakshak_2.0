import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.example.rakshak20.android.API.ApiViewmodel
import com.example.rakshak20.android.API.medicaldata
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun demopage4() {
    Box(modifier = Modifier.fillMaxSize())
    {
        var context = LocalContext.current
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
            var apiViewmodel = ApiViewmodel(context)
            Button(onClick = {
                CoroutineScope(Dispatchers.IO).launch{
                    apiViewmodel.senddataByAPI(
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
            }) {
                Text(text = "delete")
            }

        }
    }




}