package com.example.rakshak20.android

import LineChartComposable
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.rememberNavController
import com.example.rakshak20.android.navigation.navgraph
import com.example.rakshak20.android.navigation.screen
import com.example.rakshak20.android.windows.*
import demopage3
import demopage4
import io.jetchart.line.Point
import kotlin.random.Random

//import com.example.rakshak20.android.windows.SubmitDataForm

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    var navController = rememberNavController()
                    var context = LocalContext.current
//
                    navgraph( navHostController = navController, context = context, start = screen.splash.route)
//                        SubmitDataForm()
//                    RegistrationScreen(navHostController = navController, context = context)
//                    LoginScreen(navHostController = navController, context = context)
//                    fetchpage()
//                        demopage4()

                     fun points(count: Int) = (1..count).map { Point(Random.nextFloat(), "Point$it") }
//                    LineChartComposable(points(10))
                }
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
