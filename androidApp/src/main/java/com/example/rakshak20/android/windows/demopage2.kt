//package com.example.rakshak20.android.windows
//
//import androidx.compose.foundation.background
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.material.Button
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.platform.LocalContext
//import com.example.rakshak20.android.API.ApiViewmodel
//import kotlinx.coroutines.CoroutineScope
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.launch
//
//@Composable
//fun fetchpage() {
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.White))
//    {
//        var text by remember {
//            mutableStateOf("")
//        }
//        var context = LocalContext.current
//        var apiViewmodel = ApiViewmodel(context)
//        Column() {
//            Button(onClick = {
//                CoroutineScope(Dispatchers.Main).launch {
//                    try {
//                        text = apiViewmodel.getdata("1").toString()
//                    }
//                    catch (e:Exception)
//                    {
//                        text = e.message.toString()
//                    }
//                }
//            }) {
//
//            }
//
//            Text(text = text)
//
//        }
//    }
//}