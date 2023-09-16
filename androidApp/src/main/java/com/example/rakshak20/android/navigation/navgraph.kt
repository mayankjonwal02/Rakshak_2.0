package com.example.rakshak20.android.navigation

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.windows.connection
import com.example.rakshak20.android.windows.splash
import com.example.rakshak20.android.windows.visualisation

//  coe for navigation

@Composable
fun navgraph(navHostController: NavHostController,context: Context,start : String) {

    val bluetooth : MyBluetooth = viewModel()
    bluetooth.initialize(context)
    NavHost(navController = navHostController, startDestination = start)
    {
        composable(screen.splash.route)  // splash screen
        {
            splash(navHostController,context)
        }

        composable(screen.visualise.route) // visualisation screen
        {
            visualisation(context,bluetooth)
        }
        composable(screen.connection.route)
        {


            connection(navHostController,context,bluetooth)

        }

    }
}

