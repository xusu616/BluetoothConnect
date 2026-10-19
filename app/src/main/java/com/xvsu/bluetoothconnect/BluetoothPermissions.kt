package com.xvsu.bluetoothconnect

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.content.ContextCompat

/** Android 12 起，读取已配对设备和建立 HID 连接前都必须先获得此权限。 */
object BluetoothPermissions {
    @JvmStatic
    fun canConnect(context: Context): Boolean {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.S ||
            ContextCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) ==
            PackageManager.PERMISSION_GRANTED
    }
}
