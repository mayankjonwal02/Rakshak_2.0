package com.example.rakshak20.android.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rakshak20.android.functions.getSharedPreferences
import com.example.rakshak20.android.navigation.screen


@Composable
fun enterpatientid(navHostController: NavHostController) {
    var patientId by remember { mutableStateOf("") }


        // White Box
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center
        )

        // Rounded Cornered Card
        {
            Card(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
                    .wrapContentSize()
                    .padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                backgroundColor = Color.White,
                elevation = 5.dp
            ) {
                Column(
                    modifier = Modifier
                        .padding(16.dp)
                ) {
                    Text(text = "Hello Doctor !!", color = Color.Black, modifier = Modifier.padding(10.dp, bottom = 30.dp), fontWeight = FontWeight.ExtraBold, fontSize = 20.sp)
                    // Outlined Input Field
                    OutlinedTextField(
                        value = patientId,
                        onValueChange = { patientId = it },
                        label = { Text("Enter PatientID") },
                        modifier = Modifier
                            .fillMaxWidth(),
                        colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White , textColor = Color.Black, focusedBorderColor = Color.Blue, focusedLabelColor = Color.Blue , unfocusedLabelColor = Color.Gray, unfocusedBorderColor = Color.Gray)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    var context = LocalContext.current
                    var sp = getSharedPreferences(context)
                    // Blue Button
                    Button(
                        onClick = {

                            sp?.edit()?.putString("patientid", patientId)?.apply()
                            navHostController.navigate(screen.countdown.route)


                                  },
                        colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue),
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        Text(text = "Enter", fontSize = 16.sp, color = Color.White)
                    }
                }
            }
        }
    
}
