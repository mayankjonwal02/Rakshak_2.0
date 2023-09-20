package com.example.rakshak20.android.functions

import DBHandler
import PatientData
import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.compose.runtime.remember
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModel
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.Point
import com.example.rakshak20.android.constantvariables.uuid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream

//   code for bluetooth functionality

class MyBluetooth() : ViewModel() {
//    val _data = MutableLiveData<Int>(0)
//    val data : LiveData<Int> get() = _data

    lateinit var sp : SharedPreferences
    lateinit var dbHandler : DBHandler

     var patientid : String? = null


    val _navigated = MutableStateFlow<Int>(0)
    val navigated : StateFlow<Int> = _navigated

    val _paireddevices = MutableStateFlow<List<BluetoothDevice?>>(emptyList())
    val paireddevices : StateFlow<List<BluetoothDevice?>> = _paireddevices

    val _status = MutableStateFlow<String?>("Disconnected")
    val status : StateFlow<String?> = _status

    val _ECGdata = MutableStateFlow<List<Point>>(emptyList())
    val ECGdata : StateFlow<List<Point>> = _ECGdata

    val _HeartRatedata = MutableStateFlow<List<Point>>(emptyList())
    val HeartRatedata : StateFlow<List<Point>> = _HeartRatedata

    val _SPO2data = MutableStateFlow<List<Point>>(emptyList())
    val SPO2data : StateFlow<List<Point>> = _SPO2data


    val _TEMPdata = MutableStateFlow<List<Point>>(emptyList())
    val TEMPdata : StateFlow<List<Point>> = _TEMPdata

    val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private val STATE_LISTENING = 1
    private val STATE_CONNECTING = 2
    private val STATE_CONNECTED = 3
    private val STATE_CONNECTION_FAILED = 4
    private val STATE_MESSAGE_RECEIVED = 5

    lateinit var context : Context
    lateinit var connecteddevice :BluetoothDevice
    private var previous: String? = null
    private var current = null
    private var sharedvalue = ""
    private var previouslist = listOf(null,"@","#","%")
    private var currentlist = listOf(".","1","2","3","4","5","6","7","8","9","0")
    fun enablebluetooth()
    {

    }
    fun initialize(context: Context)
    {
        this.context = context
        sp = getSharedPreferences(this.context)
        dbHandler = DBHandler(this.context)
        patientid = sp.getString("patientid","").toString()
    }


    fun visualise()
    {
        _navigated.value = 1
    }

    fun connect()
    {
        _navigated.value = 0
    }

    fun fetchPairedDevices()
    {
        if(bluetoothAdapter.isNotNull())
        {

            if (ActivityCompat.checkSelfPermission(
                    context,
                    Manifest.permission.BLUETOOTH_CONNECT
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(context,"Permissions not Granted",Toast.LENGTH_LONG).show()
                return
            }
            _paireddevices.value = bluetoothAdapter.bondedDevices.toList()
        }
    }

    var handler = Handler{
        msg : Message ->
        when(msg.what)
        {
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
            STATE_MESSAGE_RECEIVED ->
            {
                val readbuff = msg.obj as ByteArray
                var data = String(readbuff,0,msg.arg1,Charsets.UTF_8)

                handledata(data)
                false
            }

            else ->
            {
                _status.value = "Error"
                false
            }
        }



    }



    fun server()
    {

    }

    fun handledata(data : String)
    {
//        Toast.makeText(context,data,Toast.LENGTH_LONG).show()
        var inputStringlist = data.split(" ").toList<String>()

        var ecg : Float? = null
        var heartrate : Float? = null
        var spo2 : Float? = null
        var temp : Float? = null
        var patientData : PatientData


        if(inputStringlist[0] == "@" && inputStringlist[1] == "@" && inputStringlist[2] == "@")
        {
            Toast.makeText(context,"Place your Finger", Toast.LENGTH_SHORT).show()
        }
        else {
            if (inputStringlist[0] != "@") {
                _ECGdata.value += Point(_ECGdata.value.size.toFloat(), inputStringlist[0].toFloat())
                ecg = inputStringlist[0].toFloat()
            } else {
                Toast.makeText(context, "ECG value out of range", Toast.LENGTH_SHORT).show()
                heartrate = inputStringlist[1].toFloat()
            }
            if (inputStringlist[1] != "@") {
                _HeartRatedata.value += Point(
                    _HeartRatedata.value.size.toFloat(),
                    inputStringlist[1].toFloat())

                spo2 = inputStringlist[2].toFloat()
            } else {
                Toast.makeText(context, "Heart Rate value out of range", Toast.LENGTH_SHORT).show()
                temp = inputStringlist[3].toFloat()
            }
            if (inputStringlist[2] != "@") {
                _SPO2data.value += Point(
                    _SPO2data.value.size.toFloat(),
                    inputStringlist[2].toFloat()
                )
            } else {
                Toast.makeText(context, "SPO2 value out of range", Toast.LENGTH_SHORT).show()
            }
            if (inputStringlist[3] != "@") {
                _TEMPdata.value += Point(
                    _TEMPdata.value.size.toFloat(),
                    inputStringlist[3].toFloat()
                )
            } else {
                Toast.makeText(context, "Temperature value out of range", Toast.LENGTH_SHORT).show()
            }

//            patientData = PatientData(patientId = patientid , ecg =  ecg , heartRate = heartrate , spo2 = spo2 , temperature = temp)

            dbHandler.addPatientData(patientid,ecg,heartrate,spo2,temp)
        }
    }

    inner class clientclass(var getdevice: BluetoothDevice) : Thread()
    {
        private lateinit var socket : BluetoothSocket
        private lateinit var device: BluetoothDevice

        init {
            client(getdevice)
        }

        fun client(getdevice: BluetoothDevice)
        {

            this.device = getdevice
            connecteddevice = getdevice
            try {
                var remotedevice = bluetoothAdapter.getRemoteDevice(this.device.address)
                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context,"Permissions not Granted",Toast.LENGTH_LONG).show()
                    return
                }
                this.socket = remotedevice.createInsecureRfcommSocketToServiceRecord(uuid)
            }
            catch (e:IOException)
            {
                e.printStackTrace()
            }
        }


        override fun run() {
            super.run()
            var message1 = Message.obtain()
            message1.what = STATE_CONNECTING
            handler.sendMessage(message1)
            try {

                if (ActivityCompat.checkSelfPermission(
                        context,
                        Manifest.permission.BLUETOOTH_CONNECT
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    Toast.makeText(context,"Permissions not Granted",Toast.LENGTH_LONG).show()
                    return
                }
                socket.connect()

                var message = Message.obtain()
                message.what = STATE_CONNECTED
                handler.sendMessage(message)
                (context as? Activity)?.runOnUiThread(){
                    Toast.makeText(context, "Connected to ${device.name}", Toast.LENGTH_LONG)
                        .show()
                }

                var receive = receivedata(socket)
                receive.start()




            }
           catch (e:IOException)
            {
                var message = Message.obtain()
                message.what = STATE_CONNECTION_FAILED
                handler.sendMessage(message)

            }
        }
    }

    inner class receivedata(socket: BluetoothSocket) : Thread()
    {
        private val bluetoothSocket : BluetoothSocket = socket
        private lateinit var inputStream: InputStream
        private lateinit var outputStream: OutputStream

        init {

        setupstream()

        }

        private fun setupstream()
        {


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

        override fun run() {
            super.run()

            var buffer = ByteArray(1024)
            var bytes : Int

            while (true)
            {
                try {
                    bytes = inputStream.read(buffer)
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED,bytes,-1,buffer).sendToTarget()
                }
                catch (e:IOException)
                {
                    e.printStackTrace()
                }
            }
        }
    }

}

data class msgdata(var key : Int , var value : Float)   // format in which we are storing incoming values