package com.example.rakshak20.android.windows

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.layout.RowScopeInstance.weight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rakshak20.android.navigation.screen


@Composable
fun option(navHostController: NavHostController) {

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.White), contentAlignment = Alignment.Center)
    {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center)
        {
            Card(backgroundColor = Color.White, contentColor = Color.Blue , modifier = Modifier
                .padding(20.dp)
                .wrapContentSize()
                .shadow(10.dp, shape = RoundedCornerShape(10.dp), spotColor = Color.Blue)
                .clickable{
                          navHostController.navigate(screen.login.route+"/Doctor")
                }
                , elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
                Text(text = "Login as Doctor", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontSize = 30.sp, fontFamily = FontFamily.Default)
            }
            Spacer(modifier = Modifier.height(50.dp))
            Card(backgroundColor = Color.White, contentColor = Color.Blue , modifier = Modifier
                .padding(20.dp)
                .wrapContentSize().shadow(10.dp, shape = RoundedCornerShape(10.dp), spotColor = Color.Blue)
                .clickable{
                    navHostController.navigate(screen.login.route+"/Patient")
                }, elevation = 10.dp, shape = RoundedCornerShape(10.dp)) {
                Text(text = "Login as Patient", modifier = Modifier.padding(20.dp), fontWeight = FontWeight.ExtraBold, fontStyle = FontStyle.Normal, fontSize = 30.sp, fontFamily = FontFamily.Default)
            }
        }
    }

}