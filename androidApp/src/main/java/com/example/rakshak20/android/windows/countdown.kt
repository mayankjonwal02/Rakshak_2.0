import android.content.Context
import android.os.CountDownTimer
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.navigation.screen
import kotlinx.coroutines.*

//import com.example.featherandroidtasks.ui.theme.FeatherAndroidTasksTheme


@Composable
fun countdownTimer(
    countdownDuration: Int,
    navHostController: NavHostController,
    context: Context,
    bluetooth: MyBluetooth,
    current_screen: MutableState<String>
) {
    var timeRemaining by remember { mutableStateOf(countdownDuration) }
    var flag by remember {
        mutableStateOf(0)
    }
    var dot = "."
    var job : Job? = null

    if(flag == 1){
        DisposableEffect(Unit) {


            val timer = object : CountDownTimer((countdownDuration * 1000).toLong(), 1000) {
                override fun onTick(millisUntilFinished: Long) {
                    timeRemaining = (millisUntilFinished / 1000).toInt()

                  CoroutineScope(Dispatchers.IO).launch {
                    bluetooth.receive.getBLEvalue()
                  }

                }

                override fun onFinish() {
//                    job?.cancel()
                    timeRemaining = 0
                    current_screen.value = "countdown"
//                    bluetooth.receive.stopThread()
                }
            }




            timer.start()
            onDispose {
                timer.cancel()

            }
        }
    }
    


    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(flag == 0)
        {
            OutlinedButton(onClick = {
                current_screen.value = "countdown-start"
                flag = 1}, border = BorderStroke(3.dp,Color.Blue),colors = ButtonDefaults.outlinedButtonColors(backgroundColor = Color.Transparent, contentColor = Color.Blue,)) {
                Text(text = "START")
            }
        }
        else
        {
            if(current_screen.value == "countdown-start"){
                Text(
                    text = "Fetching Data" + dot.repeat(5 - timeRemaining % 5),
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = timeRemaining.toString(),
                    color = Color.Magenta,
                    fontStyle = FontStyle.Normal,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Default
                )
            }
            else
            {
                Text(
                    text = "Click to Visualize",
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center, color = Color.Black
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
            if(timeRemaining == 0)
            {

                Button(onClick = {
                    current_screen.value = "countdown"
                    navHostController.navigate(screen.visualise.route) }) {
                    Text(text = "Visualize Now")
                }


            }
        }
    }

    if(timeRemaining == 0)
    {
        bluetooth.receive.stopThread()
    }


}
