import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.Blue
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.graphics.Color.Companion.Transparent
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.toUpperCase
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.extensions.isNotNull
import com.example.rakshak20.android.functions.MyBluetooth
import com.example.rakshak20.android.windows.SCREEN1
import com.example.rakshak20.android.windows.SCREEN2
import com.example.rakshak20.android.windows.SCREEN3
import io.jetchart.common.animation.fadeInAnimation
import io.jetchart.line.Line
import io.jetchart.line.LineChart
import io.jetchart.line.Point
import io.jetchart.line.renderer.line.GradientLineShader
import io.jetchart.line.renderer.line.SolidLineDrawer
import io.jetchart.line.renderer.point.FilledPointDrawer
import io.jetchart.line.renderer.xaxis.LineXAxisDrawer
import io.jetchart.line.renderer.yaxis.LineYAxisWithValueDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.random.Random

@Composable
fun LineChartComposable(myBluetooth: MyBluetooth , type : String) {
    val scrollState = rememberScrollState()
    var data = if (type == SCREEN1) {
        myBluetooth.ECGdata.collectAsState(emptyList()).value
    } else if (type == SCREEN2) {
        myBluetooth.HeartRatedata.collectAsState(emptyList()).value
    } else if (type == SCREEN3) {
        myBluetooth.SPO2data.collectAsState(emptyList()).value
    } else {
        myBluetooth.TEMPdata.collectAsState(emptyList()).value
    }
    val chartModifier = Modifier
        .horizontalScroll(scrollState)
        .onGloballyPositioned {
            // Scroll to the end (latest data) when the component is first displayed or when new data is added
            CoroutineScope(Dispatchers.IO).launch {
                scrollState.scrollTo(scrollState.maxValue)
            }

        }
        .width(600.dp) // Adjust the width here
        .height(300.dp) // Adjust the height here
    Box(modifier = Modifier.height(200.dp), contentAlignment = Alignment.Center) {
        if(type == SCREEN1){
            if (data.size > 5) {
//            Column(modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()){
//                Text(text = type.toString().toUpperCase(), color = Black, fontStyle = FontStyle.Normal, fontSize = 30.sp, fontWeight = FontWeight.ExtraBold, fontFamily = FontFamily.Default)
                Spacer(modifier = Modifier.height(20.dp))

//            var data20 = data.takeLast(20)
//            var datalast = data20.last()
                LineChart(
                    lines = listOf(
                        Line(
                            points = data,
                            lineDrawer = SolidLineDrawer(thickness = 8.dp, color = Blue)
                        ),
                    ),
                    modifier = Modifier
                        .horizontalScroll(rememberScrollState())
                        .width(400.dp)
                        .height(400.dp),

                    animation = fadeInAnimation(3000),
                    pointDrawer = FilledPointDrawer(),

                    xAxisDrawer = LineXAxisDrawer(),
                    yAxisDrawer = LineYAxisWithValueDrawer(),
                    horizontalOffsetPercentage = 1f,
                    lineShader = GradientLineShader(listOf(Blue, Transparent))
                )
//            }
            } else {
                Text(
                    text = "No Data to show....",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Transparent
                        ),
                    color = Blue
                )
            }
        }
        else
        {
            if(data.size > 0)
            {
                var values = data.toList().filterNotNull().filter { it.value != 0f }.map { it.value.toDouble() }
                if(values.size > 0)
                {
                    var avgvalue = values.sum()/values.size
                    Text(
                        text = "Average $type : " + avgvalue.toFloat().toString(),
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Transparent
                            ),
                        color = Blue,

                        fontFamily = FontFamily.Default,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        fontStyle = FontStyle.Normal
                    )
                }
                else
                {
                    Text(
                        text = "No Data to show....",
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Transparent
                            ),
                        color = Blue
                    )
                }

            }
            else
            {
                Text(
                    text = "No Data to show....",
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            Transparent
                        ),
                    color = Blue
                )
            }
        }

    }
}




