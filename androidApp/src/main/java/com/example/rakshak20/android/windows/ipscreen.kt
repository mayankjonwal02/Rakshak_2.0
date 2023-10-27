package com.example.rakshak20.android.windows

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rakshak20.android.functions.getSharedPreferences
import com.example.rakshak20.android.navigation.screen

@Composable
fun ipscreen(context: Context, navHostController: NavHostController) {
    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.TopCenter)
    {

        var sharedPreferences =  remember {
            getSharedPreferences(context)
        }
        var status by remember {
            mutableStateOf("")
        }

        var ipaddress by remember {
            mutableStateOf(sharedPreferences?.getString("ipaddress","0.0.0.0"))
        }



        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.background(
            Color.White).verticalScroll(
            rememberScrollState()
        )){
            Text(text = "Server address", color = Color.Black, fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, fontStyle = FontStyle.Normal, fontFamily = FontFamily.Default, modifier = Modifier.padding(top = 50.dp))
            Spacer(modifier = Modifier.height(90.dp))
            ipaddress?.let { it ->
                OutlinedTextField(
                    value = it,
                    onValueChange = { ipaddress = it },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        focusedBorderColor = Color.Green,
                        textColor = Color.Black,
                        focusedLabelColor = Color.Green,
                        unfocusedLabelColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        backgroundColor = Color.White,
                        cursorColor = Color.Black,
                        placeholderColor = Color.LightGray
                    ),
                    label = { Text(text = "IP Address") },
                    placeholder = { Text(text = "Enter IP Address") }
                )
            }
            Spacer(modifier = Modifier.height(5.dp))
            Button(onClick = {

                sharedPreferences?.edit()?.putString("ipaddress",ipaddress)?.apply()
                if(sharedPreferences?.getString("patientid","") == "" || sharedPreferences?.getString("password","") == "")
                {
                    navHostController.navigate(screen.login.route)
                }
                else
                {
                    navHostController.navigate(screen.main.route)
                }


            } , colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue)) {
                Text(text = "Connect" , color = Color.White)
            }
            Spacer(modifier = Modifier.height(70.dp))
            Text(text = status,color = Color.Black)
            Spacer(modifier = Modifier.height(90.dp))

        } }
}