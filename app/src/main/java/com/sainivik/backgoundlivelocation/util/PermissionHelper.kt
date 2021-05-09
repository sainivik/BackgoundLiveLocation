package com.sainivik.backgoundlivelocation.util

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.sainivik.backgoundlivelocation.util.PermissionHelper.Permissions.Companion.MY_PERMISSIONS_REQUEST_LOCATION


class PermissionHelper {
    companion object {

        fun checkLocationPermission(activity: Activity): Boolean {
            return ContextCompat.checkSelfPermission(
                activity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        }


        fun requestLocationPermission(activity: Activity) {
            if (ContextCompat.checkSelfPermission(
                    activity,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    activity,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    MY_PERMISSIONS_REQUEST_LOCATION
                )

            }
        }


    }


    interface Permissions {
        companion object {

            const val MY_PERMISSIONS_REQUEST_LOCATION = 222
            const val MY_IGNORE_OPTIMIZATION_REQUEST = 3333
        }
    }


}
