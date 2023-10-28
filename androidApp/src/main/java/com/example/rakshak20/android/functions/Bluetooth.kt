package com.example.rakshak20.android.functions

import DBHandler
import PatientData
import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import co.yml.charts.common.extensions.isNotNull
import io.jetchart.line.Point
import com.example.rakshak20.android.constantvariables.uuid
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//   code for bluetooth functionality

class MyBluetooth() : ViewModel() {
//    val _data = MutableLiveData<Int>(0)
//    val data : LiveData<Int> get() = _data

    lateinit var sp: SharedPreferences
    lateinit var dbHandler: DBHandler

    lateinit var receive: MyBluetooth.receivedata

    var patientid: String? = null

    var socket: BluetoothSocket? = null
    lateinit var device: BluetoothDevice


    @Volatile
    var getmessage: Boolean = false


    val _navigated = MutableStateFlow<Int>(0)
    val navigated: StateFlow<Int> = _navigated

    val _paireddevices = MutableStateFlow<List<BluetoothDevice?>>(emptyList())
    val paireddevices: StateFlow<List<BluetoothDevice?>> = _paireddevices

    val _status = MutableStateFlow<String?>("Disconnected")
    val status: StateFlow<String?> = _status

    val _ECGdata = MutableStateFlow<List<Point>>(emptyList())
    val ECGdata: StateFlow<List<Point>> = _ECGdata

    val _HeartRatedata = MutableStateFlow<List<Point>>(emptyList())
    val HeartRatedata: StateFlow<List<Point>> = _HeartRatedata

    val _SPO2data = MutableStateFlow<List<Point>>(emptyList())
    val SPO2data: StateFlow<List<Point>> = _SPO2data


    val _TEMPdata = MutableStateFlow<List<Point>>(emptyList())
    val TEMPdata: StateFlow<List<Point>> = _TEMPdata

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val STATE_LISTENING = 1
    private val STATE_CONNECTING = 2
    private val STATE_CONNECTED = 3
    private val STATE_CONNECTION_FAILED = 4
    private val STATE_MESSAGE_RECEIVED = 5

    lateinit var context: Context
    lateinit var connecteddevice: BluetoothDevice
    private var previous: String? = null
    private var current = null
    private var sharedvalue = ""
    private var previouslist = listOf(null, "@", "#", "%")
    private var currentlist = listOf(".", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0")
    private var ecg: String? = ""
    private var heartrate: String? = ""
    private var spo2: String? = ""
    private var temp: String? = ""
    private var pre_delimiter = ""

    fun enablebluetooth() {

    }

    fun initialize(context: Context) {
        this.context = context
        sp = getSharedPreferences(this.context)
        dbHandler = DBHandler(this.context)
        patientid = sp.getString("patientid", "").toString()
    }


    fun visualise() {
        _navigated.value = 1
    }

    fun connect() {
        _navigated.value = 0
    }

    @SuppressLint("MissingPermission")
    fun fetchPairedDevices() {
        if (bluetoothAdapter.isNotNull()) {

//            if (ActivityCompat.checkSelfPermission(
//                    context,
//                    Manifest.permission.BLUETOOTH_CONNECT
//                ) != PackageManager.PERMISSION_GRANTED
//            ) {
//                Toast.makeText(context, "Permissions not Granted", Toast.LENGTH_LONG).show()
//                return
//            }
            _paireddevices.value = bluetoothAdapter.bondedDevices.toList()
        }
    }

    var handler = Handler { msg: Message ->
        when (msg.what) {
            STATE_LISTENING -> {
                _status.value = "Listening..."
                false
            }
            STATE_CONNECTING -> {
                _status.value = "Connecting..."
                false
            }
            STATE_CONNECTED -> {
                _status.value = "Connected"
                false
            }
            STATE_CONNECTION_FAILED -> {
                _status.value = "Connection Failed"
                false
            }
            STATE_MESSAGE_RECEIVED -> {
                val readbuff = msg.obj as ByteArray
                var data = String(readbuff, 0, msg.arg1, Charsets.UTF_8)

                if(data.isNotEmpty() || data.isNotBlank()){
                    handledata(data)
                }
                else {
                    Toast.makeText(context, "Data not received", Toast.LENGTH_SHORT).show()
                }
                false
            }

            else -> {
                _status.value = "Error"
                false
            }
        }


    }


    fun server() {

    }

    fun handledata(data: String) {
//        Toast.makeText(context,data,Toast.LENGTH_LONG).show()
        Log.i("TAG1", data)
        val delimiter = setOf('!', '#', '$', '%', '*')
        val values = setOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '@')

        for (i in data) {
            if (i in delimiter) {
                pre_delimiter = i.toString()
                if (i == '*') {
                    handleCompleteData()
                }
            } else if (i in values) {
                if (i != '@') {
                    handleValue(i)
                } else {
                    handleNullValue()
                }
            }
        }
    }

    private fun handleValue(value: Char) {
        when (pre_delimiter) {
            "!" -> ecg += value
            "#" -> heartrate += value
            "$" -> spo2 += value
            "%" -> temp += value
        }
    }

    private fun handleNullValue() {
        when (pre_delimiter) {
            "!" -> ecg = "0"
            "#" -> heartrate = "0"
            "$" -> spo2 = "0"
            "%" -> temp = "0"
        }
    }

    private fun handleCompleteData() {
//        Log.i("TAG1", "$ecg $heartrate $spo2 $temp")
//        Toast.makeText(context,"$ecg $heartrate $spo2 $temp",Toast.LENGTH_SHORT).show()
        dbHandler.addPatientData(
            patientId = patientid,
            ecg = ecg.toString().toFloat(),
            heartRate = heartrate.toString().toFloat(),
            spo2 = spo2.toString().toFloat(),
            temperature = temp.toString().toFloat()
        )

        if (ecg != "0" || ecg != "" ) {
            _ECGdata.value =
                _ECGdata.value + Point(
                    ecg.toString().toFloat(),
                    _ECGdata.value.size.toFloat().toString()
                )
        }
        if (heartrate != "0" || heartrate != "") {
            _HeartRatedata.value = _HeartRatedata.value + Point(

                heartrate.toString().toFloat(),
                _HeartRatedata.value.size.toFloat().toString(),
            )
        }
        if (spo2 != "0" || spo2 != "") {
            _SPO2data.value =
                _SPO2data.value + Point(
                    spo2.toString().toFloat(),
                    _SPO2data.value.size.toFloat().toString()
                )
        }
        if (temp != "0" || temp != "") {
            _TEMPdata.value =
                _TEMPdata.value + Point(
                    temp.toString().toFloat(),
                    _TEMPdata.value.size.toFloat().toString()
                )
        }

        // Reset the variables for the next set of data
        ecg = ""
        heartrate = ""
        spo2 = ""
        temp = ""
    }


    fun handledata1(data: String) {
        Toast.makeText(context, data, Toast.LENGTH_LONG).show()
        Log.i("TAG1", data)
        var delimiter = listOf('!', '#', '$', '%', '*')
        var values = listOf('1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '.', '@')
        for (i in data) {
            if (i in delimiter) {
                pre_delimiter = i.toString()
                if (i.toString() == "*") {
                    Toast.makeText(context, "$ecg $heartrate $spo2 $temp", Toast.LENGTH_LONG).show()
                    Log.i("TAG1", "$ecg $heartrate $spo2 $temp")
                    temp = ""
                    heartrate = ""
                    spo2 = ""
                    temp = ""

                }
            } else if (i in values) {
                if (i != '@') {
                    if (pre_delimiter == "!") {
                        ecg += i.toString()
                    }
                    if (pre_delimiter == "#") {
                        ecg = ""
                        heartrate += i.toString()
                    }
                    if (pre_delimiter == "$") {
                        heartrate = ""
                        spo2 += i.toString()
                    }
                    if (pre_delimiter == "%") {
                        spo2 = ""
                        temp += i.toString()
                    }
                } else {
                    if (pre_delimiter == "!") {
                        ecg = "null"
                    }
                    if (pre_delimiter == "#") {

                        heartrate = "null"
                    }
                    if (pre_delimiter == "$") {

                        spo2 = "null"
                    }
                    if (pre_delimiter == "%") {

                        temp = "null"
                    }
                }
            }
        }


    }

    inner class clientclass(var getdevice: BluetoothDevice) : Thread() {
//        private lateinit var socket: BluetoothSocket
//        private lateinit var device: BluetoothDevice


        init {
            client(getdevice)

        }

        @SuppressLint("MissingPermission")
        fun client(getdevice: BluetoothDevice) {

            device = getdevice
            connecteddevice = getdevice
            try {
                var remotedevice = bluetoothAdapter.getRemoteDevice(device.address)
//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    Handler(Looper.getMainLooper()).post {
//                        Toast.makeText(context, "Permissions not Granted", Toast.LENGTH_LONG).show()
//                    }
//                    return
//                }
                socket = remotedevice.createInsecureRfcommSocketToServiceRecord(uuid)

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


        @SuppressLint("MissingPermission")
        override fun run() {
            super.run()
            var message1 = Message.obtain()
            message1.what = STATE_CONNECTING
            handler.sendMessage(message1)
            try {

//                if (ActivityCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.BLUETOOTH_CONNECT
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    Toast.makeText(context, "Permissions not Granted", Toast.LENGTH_LONG).show()Handler(Looper.getMainLooper()).post {
//                        Toast.makeText(context, "Permissions not Granted", Toast.LENGTH_LONG).show()
//                    }
//                    return
//                }
                socket?.connect()

                var message = Message.obtain()
                message.what = STATE_CONNECTED
                handler.sendMessage(message)
                (context as? Activity)?.runOnUiThread() {
                    // Show a Toast on the main/UI thread
                    Handler(Looper.getMainLooper()).post {
                        Toast.makeText(context, "Connected to ${device.name}", Toast.LENGTH_LONG).show()
                    }
                }

                receive = receivedata(socket!!)
//                receive.startThread()
//                receive.start()


            } catch (e: IOException) {
                var message = Message.obtain()
                message.what = STATE_CONNECTION_FAILED
                handler.sendMessage(message)

            }
        }
    }

    inner class receivedata(socket: BluetoothSocket)  {
        private val bluetoothSocket: BluetoothSocket = socket
        private lateinit var inputStream: InputStream
        private lateinit var outputStream: OutputStream
        private var isRunning: Boolean = false // Flag to control thread execution

        init {
            setupstream()
        }

        private fun setupstream() {
            var tempIn: InputStream? = null
            var tempOut: OutputStream? = null

            try {
                tempIn = bluetoothSocket.inputStream
                tempOut = bluetoothSocket.outputStream
            } catch (e: IOException) {
                e.printStackTrace()
            }

            inputStream = tempIn ?: return
            outputStream = tempOut ?: return
        }

        suspend fun getBLEvalue()
        {
            var buffer = ByteArray(1024)
            var bytes: Int
            Log.e("TAG1",isRunning.toString())
            try {
                bytes = inputStream.read(buffer)
                handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer)
                    .sendToTarget()
            } catch (e: IOException) {
                e.printStackTrace()
                withContext(Dispatchers.Main) {
                    Toast.makeText(context, "Error parsing data", Toast.LENGTH_SHORT).show()
                }//                        isRunning = false // Stop the thread on IO exception
            }

        }

//        override fun run() {
//            super.run()
//
//            var buffer = ByteArray(1024)
//            var bytes: Int
//
//
//            while (true) { // Check the flag to determine if the thread should continue
//                Log.e("TAG1",isRunning.toString())
//
//                if(isRunning){
//                    try {
//                        bytes = inputStream.read(buffer)
//                        handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1, buffer)
//                            .sendToTarget()
//                    } catch (e: IOException) {
//                        e.printStackTrace()
////                        isRunning = false // Stop the thread on IO exception
//                    }
//                }
//            }
//        }
        fun startThread()
        {
            isRunning = true
        }
        fun stopThread() {
            isRunning = false
        }
    }
}

    data class msgdata(var key : Int , var value : Float)   // format in which we are storing incoming values