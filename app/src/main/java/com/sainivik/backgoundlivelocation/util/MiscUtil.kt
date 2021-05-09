package com.sainivik.backgoundlivelocation.util

import android.app.Activity
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.os.PowerManager
import android.text.format.DateFormat
import androidx.core.app.NotificationManagerCompat
import java.util.*


object MiscUtil {


    fun checkAllRequiredPermission(context: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            return PermissionHelper.checkLocationPermission(context) && checkGPSPermission(context) && MiscUtil.checkNotificationIsEnabled(
                context
            ) && checkBatteryOptimization(
                context
            )
        } else {
            return PermissionHelper.checkLocationPermission(context) && checkGPSPermission(context) && MiscUtil.checkNotificationIsEnabled(
                context
            )
        }
    }


    fun checkBatteryOptimization(context: Activity): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            val pm =
                context.getSystemService(Context.POWER_SERVICE) as PowerManager
            val isIgnoringBatteryOptimizations =
                pm.isIgnoringBatteryOptimizations(context.packageName)
            return isIgnoringBatteryOptimizations
        } else {
            return true
        }
    }


    private fun checkGPSPermission(context: Activity): Boolean {
        val locationManager =
            context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    fun checkNotificationIsEnabled(context: Context): Boolean {
        return NotificationManagerCompat.from(context).areNotificationsEnabled()
    }

    fun getDate(time: Long): String {
        try {
            val cal: Calendar = Calendar.getInstance(Locale.ENGLISH)
            cal.setTimeInMillis(time)
            return DateFormat.format("dd-MM-yyyy HH:mm:ss aa", cal).toString()
        } catch (e: Exception) {
            return ""
        }
    }
}