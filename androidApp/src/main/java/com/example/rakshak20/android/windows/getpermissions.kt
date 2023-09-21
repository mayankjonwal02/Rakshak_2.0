package com.example.rakshak20.android.windows

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.core.content.ContextCompat.startActivity

@Composable
fun getpermissions(context: Context) {

    var permissionlauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        )
    {
//            permissions ->
//        val allPermissionsGranted = permissions.values.all { it }
//        if (allPermissionsGranted) {
//            // All permissions granted, you can proceed with Bluetooth-related functionality
//        } else {
//            // At least one permission is not granted, prompt the user to go to app settings
//            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
//            val uri = Uri.fromParts("com.example.rakshak20", context.packageName, null)
//            intent.data = uri
//            context.startActivity(intent)
//        }
    }

    LaunchedEffect(Unit)
    {
        permissionlauncher.launch(arrayOf(
            Manifest.permission.BLUETOOTH_CONNECT,
            Manifest.permission.BLUETOOTH,
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.BLUETOOTH_ADVERTISE,
            Manifest.permission.BLUETOOTH_SCAN,
            Manifest.permission.BLUETOOTH_ADMIN
        ))
    }


}