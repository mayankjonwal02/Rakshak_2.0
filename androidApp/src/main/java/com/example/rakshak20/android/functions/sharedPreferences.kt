package com.example.rakshak20.android.functions

import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.SharedPreferences
import android.view.Display.Mode
import com.google.gson.Gson

fun getSharedPreferences(context: Context): SharedPreferences {
    return context.getSharedPreferences("my_shared_prefs",Context.MODE_PRIVATE)
}


fun saveBluetoothDeviceInfo(deviceInfo: BluetoothDeviceInfo, context: Context) {
    val sharedPreferences = getSharedPreferences(context)
    val editor = sharedPreferences!!.edit()

    // Serialize the BluetoothDeviceInfo to JSON
    val jsonDeviceInfo = Gson().toJson(deviceInfo)

    editor.putString("bluetooth_device_info", jsonDeviceInfo)
    editor.apply()
}



fun getBluetoothDeviceInfo(context: Context): BluetoothDeviceInfo? {
    val sharedPreferences = getSharedPreferences(context)
    val jsonDeviceInfo = sharedPreferences?.getString("bluetooth_device_info", null)

    return if (jsonDeviceInfo != null) {
        // Deserialize the JSON to BluetoothDeviceInfo
        Gson().fromJson(jsonDeviceInfo, BluetoothDeviceInfo::class.java)
    } else {
        null
    }
}



data class BluetoothDeviceInfo(
    val deviceName: String,
    val deviceAddress: String
)
