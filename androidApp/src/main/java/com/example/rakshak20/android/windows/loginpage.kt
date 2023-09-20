package com.example.rakshak20.android.windows
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toLowerCase
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rakshak20.android.API.ApiViewmodel
import com.example.rakshak20.android.API.LoginRequest
import com.example.rakshak20.android.API.Registrationdata
//import com.example.rakshak20.android.API.LoginRequest
import com.example.rakshak20.android.functions.getSharedPreferences
import com.example.rakshak20.android.navigation.screen
import kotlinx.coroutines.*
import java.io.IOException

//import com.example.rakshak20.android.functions.getShredPreferences

@Composable
fun LoginScreen(navHostController: NavHostController, context: Context) {
    var sp = remember{ getSharedPreferences(context) }
    var patientId by remember { mutableStateOf(sp?.getString("patientid","")) }
    var password by remember { mutableStateOf(sp?.getString("password","")) }
    var isPasswordVisible by remember { mutableStateOf(false) }



    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center)
    {
        Card(modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(25.dp)
            .shadow(20.dp, spotColor = Color.Blue, shape = RoundedCornerShape(20.dp)), backgroundColor = Color.White, elevation = 20.dp, shape = RoundedCornerShape(20.dp)
        ) {
           Column(modifier = Modifier
               .background(Color.Transparent)
               .padding(10.dp), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
               Spacer(modifier = Modifier.height(20.dp))
               Text(
                   text = "Login", color = Color.Blue,
                   modifier = Modifier
                       .fillMaxWidth()
                       .background(
                           Color.White
                       ), fontWeight = FontWeight.ExtraBold, fontSize = 30.sp, fontStyle = FontStyle.Normal, textAlign = TextAlign.Center, fontFamily = FontFamily.Serif
               )
               Spacer(modifier = Modifier.height(20.dp))
               patientId?.let {
                   OutlinedTextField(
                       value = it,
                       onValueChange = { patientId = it },
                       label = { Text("Patient ID") },
                       singleLine = true,
                       keyboardOptions = KeyboardOptions.Default.copy(
                           keyboardType = KeyboardType.Text
                       ), colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White, textColor = Color.Blue, focusedLabelColor = Color.Blue, focusedBorderColor = Color.Blue, cursorColor = Color.Blue),
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(vertical = 8.dp)
                   )
               }

               password?.let {
                   OutlinedTextField(
                       value = it,
                       onValueChange = { password = it },
                       label = { Text("Password") },
                       singleLine = true,
                       keyboardOptions = KeyboardOptions.Default.copy(
                           keyboardType = KeyboardType.Password
                       ),
                       trailingIcon = {
                           IconButton(onClick = {
                               isPasswordVisible = !isPasswordVisible
                           }) {
                               Icon(
                                   imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff
                                   else Icons.Default.Visibility,
                                   contentDescription = null
                               )
                           }
                       },
                       visualTransformation = if (isPasswordVisible) VisualTransformation.None
                       else PasswordVisualTransformation(),
                       keyboardActions = KeyboardActions(
                           onDone = {
                               // Handle login
                           }
                       ), colors = TextFieldDefaults.outlinedTextFieldColors(backgroundColor = Color.White, textColor = Color.Blue, focusedLabelColor = Color.Blue, focusedBorderColor = Color.Blue, cursorColor = Color.Blue),
                       modifier = Modifier
                           .fillMaxWidth()
                           .padding(vertical = 8.dp)
                   )
               }
               var apiViewmodel = ApiViewmodel(context)
               Button(
                   onClick = {
                       CoroutineScope(Dispatchers.Main).launch {
                           try {
                               val loginRequest = LoginRequest(patientid = patientId!!, password = password!!)
                               val response = apiViewmodel.loginByAPI(loginRequest)
                               GlobalScope.launch(Dispatchers.Main) {
                                   Toast.makeText(context, response, Toast.LENGTH_LONG).show()
                               }
                                   if(response.toString().equals("ok"))
                                   {
                                       sp?.edit()?.putString("patientid",patientId)?.apply()
                                       sp?.edit()?.putString("password",password)?.apply()
                                       navHostController.navigate(screen.main.route)
                                   }
                                   Log.e("TAG1",response.toString())

                           } catch (e: Exception) {
                               // Handle the exception appropriately, e.g., display an error message

                               GlobalScope.launch(Dispatchers.Main){
                                   Toast.makeText(context, "Error: ${e.message}", Toast.LENGTH_LONG)
                                       .show()
                               }

                           }
                       }


                   },
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(vertical = 16.dp, horizontal = 30.dp),
                   colors = ButtonDefaults.buttonColors(backgroundColor = Color.Blue
                       , contentColor = Color.White)
               ) {
                   Text(text = "Login")
               }
               Text(text = "Not Registered Yet ? Sign Up", modifier = Modifier.clickable{navHostController.navigate(screen.registration.route)})

           }
        }
    }
}




@Composable
fun RegistrationScreen(navHostController: NavHostController, context: Context) {
    var patientId by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmpassword by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }
    var isPasswordVisible1 by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }
    var age by remember { mutableStateOf("") }
    var gender by remember { mutableStateOf("") }
    var height by remember { mutableStateOf("") }
    var weight by remember { mutableStateOf("") }

    var apiViewmodel = ApiViewmodel(context)


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White), contentAlignment = Alignment.Center
    )
    {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(25.dp)
                .shadow(20.dp, spotColor = Color.Blue, shape = RoundedCornerShape(20.dp)),
            backgroundColor = Color.White,
            elevation = 20.dp,
            shape = RoundedCornerShape(20.dp)
        ) {
            Column(
                modifier = Modifier
                    .background(Color.Transparent)
                    .verticalScroll(rememberScrollState())
                    .padding(10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = "Registration",
                    color = Color.Blue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Color.White
                        ),
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 30.sp,
                    fontStyle = FontStyle.Normal,
                    textAlign = TextAlign.Center,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(20.dp))
                OutlinedTextField(
                    value = patientId,
                    onValueChange = { patientId = it },
                    label = { Text("Patient ID") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = age,
                    onValueChange = { age = it },
                    label = { Text("Age") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = gender,
                    onValueChange = { gender = it },
                    label = { Text("Gender") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Text
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = height,
                    onValueChange = { height = it },
                    label = { Text("Height (in cm)") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = weight,
                    onValueChange = { weight = it },
                    label = { Text("Weight (in Kg)") },
                    singleLine = true,

                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Number
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )



                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible = !isPasswordVisible
                        }) {
                            Icon(
                                imageVector = if (isPasswordVisible) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                OutlinedTextField(
                    value = confirmpassword,
                    onValueChange = { confirmpassword = it },
                    label = { Text("Confirm Password") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            isPasswordVisible1 = !isPasswordVisible1
                        }) {
                            Icon(
                                imageVector = if (isPasswordVisible1) Icons.Default.VisibilityOff
                                else Icons.Default.Visibility,
                                contentDescription = null
                            )
                        }
                    },
                    visualTransformation = if (isPasswordVisible1) VisualTransformation.None
                    else PasswordVisualTransformation(),
                    keyboardOptions = KeyboardOptions.Default.copy(
                        keyboardType = KeyboardType.Password
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            // Handle login
                        }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        backgroundColor = Color.White,
                        textColor = Color.Blue,
                        focusedLabelColor = Color.Blue,
                        focusedBorderColor = Color.Blue,
                        cursorColor = Color.Blue
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                Button(
                    onClick = {

                        if(patientId.isNotBlank() && name.isNotBlank() && age.isNotBlank() && gender.isNotBlank() && height.isNotBlank() && weight.isNotBlank() && password.isNotBlank())
                        {
                            if(gender.toLowerCase() in listOf("male","female") && age.toInt() in 0..150  ){
                                if(password.length >= 6){
                                    CoroutineScope(Dispatchers.Main).launch {
                                        try {
                                            val response = apiViewmodel.registerByAPI(
                                                Registrationdata(
                                                    patientid = patientId,
                                                    name = name,
                                                    age = age.toInt(),
                                                    gender = gender,
                                                    height = height.toFloat(),
                                                    weight = weight.toFloat(),
                                                    password = password
                                                )
                                            )

                                            // Update UI on the main thread
                                            GlobalScope.launch(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    response,
                                                    Toast.LENGTH_SHORT
                                                ).show()

                                                navHostController.navigate(screen.login.route       )
                                            }
                                        } catch (e: Exception) {
                                            // Handle the exception appropriately

                                            // Update UI on the main thread
                                            withContext(Dispatchers.Main) {
                                                Toast.makeText(
                                                    context,
                                                    "Error: ${e.message}",
                                                    Toast.LENGTH_SHORT
                                                ).show()
                                                Log.e("TAG1", e.message.toString())
                                            }
                                        }
                                    }
                                }
                                else
                                {
                                    Toast.makeText(context, "Password Length should be greater than 6", Toast.LENGTH_SHORT).show()
                                }
                            }
                            else
                            {
                                Toast.makeText(context, "Invalid Data", Toast.LENGTH_SHORT).show()
                            }
                        }
                        else
                        {
                            Toast.makeText(context, "Fields Empty", Toast.LENGTH_SHORT).show()
                        }


                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 30.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Blue, contentColor = Color.White
                    )
                ) {
                    Text(text = "Register")
                }
                Text(
                    text = "Already Registered ? Sign In",
                    modifier = Modifier.clickable { navHostController.navigate(screen.login.route) })
            }
        }
    }
}

