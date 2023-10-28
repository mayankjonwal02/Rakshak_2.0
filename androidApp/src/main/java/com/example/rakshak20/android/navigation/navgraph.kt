package com.example.rakshak20.android.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.rememberUpdatedState
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.windows.*
import countdownTimer

//
//  coe for navigation
var bluetooth : MyBluetooth? = null

@Composable

fun navgraph(


    navHostController: NavHostController,
    context: Context,
    start: String) {


    NavHost(navController = navHostController, startDestination = start)
    {
        composable(screen.splash.route)  // splash screen
        {
            splash(navHostController,context)
        }



        composable(screen.login.route)
        {
            LoginScreen(navHostController,context)
        }

        composable(screen.registration.route)
        {
            RegistrationScreen(navHostController,context)
        }

        composable(screen.main.route)
        {
            mainwindow(navHostController,context)
        }

        composable(screen.ipscreen.route)
        {
            ipscreen(context = context, navHostController)
        }



    }
}



@Composable
fun navgraph1(
    oldnavcon : NavController,
    navHostController: NavHostController,
    context: Context,
    start: String) {

    bluetooth = MyBluetooth()
    bluetooth!!.initialize(context)
    NavHost(navController = navHostController, startDestination = start)
    {


        composable(screen.visualise.route) // visualisation screen
        {
            visualisation(context, bluetooth!!)
        }
        composable(screen.connection.route)
        {


            connection(navHostController,context, bluetooth!!)

        }



        composable(screen.countdown.route)
        {

            countdownTimer(countdownDuration = 60, navHostController , context , bluetooth!!)
        }





    }
}



