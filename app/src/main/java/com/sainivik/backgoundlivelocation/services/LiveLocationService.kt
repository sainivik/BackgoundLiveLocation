package com.sainivik.backgoundlivelocation.services

import android.Manifest
import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.database.MyAppDatabase
import com.sainivik.backgoundlivelocation.model.LocationTable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

open class LiveLocationService : Service() {
    companion object {
        val ACTION_START_FOREGROUND_SERVICE = "ACTION_START_FOREGROUND_SERVICE"
        val ACTION_STOP_FOREGROUND_SERVICE = "ACTION_STOP_FOREGROUND_SERVICE"
    }

    open var intent: Intent? = null
    var locationManager: LocationManager? = null
    var listener: MyLocationListener? = null
    var previousBestLocation: Location? = null
    var notificationId = 1909


    override fun onCreate() {
        super.onCreate()


    }


    override fun onBind(intent: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d("LiveLocationService", "I'm onStartCommand")
        if (intent != null) {
            var action = intent.action
            if (action != null) {
                when (action) {
                    ACTION_START_FOREGROUND_SERVICE -> {
                        startForegroundService(locationServiceNotification(this)!!)
                        locationManager =
                            getSystemService(Context.LOCATION_SERVICE) as LocationManager
                        listener = MyLocationListener()
                        if (ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_FINE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                                this,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ) != PackageManager.PERMISSION_GRANTED
                        ) {
                        } else {
                            locationManager!!.requestLocationUpdates(
                                LocationManager.NETWORK_PROVIDER,
                                10000,
                                0f,
                                listener as LocationListener
                            )
                            locationManager!!.requestLocationUpdates(
                                LocationManager.GPS_PROVIDER,
                                10000,
                                0f,
                                listener!!
                            )

                        }


                    }
                    ACTION_STOP_FOREGROUND_SERVICE -> {
                        stopForegroundService()
                    }

                }
            }

        }


        return START_STICKY
    }


    private fun isSameProvider(
        provider1: String?,
        provider2: String?
    ): Boolean {
        return if (provider1 == null) {
            provider2 == null
        } else provider1 == provider2
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("LiveLocationService", "I'm Stopping")
    }

    inner class MyLocationListener : LocationListener {
        val TWO_MINUTES = 1000 * 60 * 2
        override fun onLocationChanged(loc: Location) {
            Log.i("LiveLocationService>>", "Location changed")
            if (previousBestLocation == null) {
                previousBestLocation = loc
                return
            }


            if (isBetterLocation(loc, previousBestLocation!!)) {
                updateLocation(loc)
                loc.latitude
                loc.longitude

                /* intent?.putExtra("Latitude", loc.latitude)
                 intent?.putExtra("Longitude", loc.longitude)
                 intent?.putExtra("Provider", loc.provider)
                 sendBroadcast(intent)*/
            }
        }

        fun updateLocation(location: Location) {

            //  val df2 = DecimalFormat("#.####")
            val location1 = LocationTable(
                location.time,
                location.latitude,
                location.longitude,
                location.time,
                location.bearing.toDouble(),
                location.accuracy.toDouble(),
                location.altitude,
                0, 0, 1


            )

            SaveLocationToLocalDB(location1!!)

            previousBestLocation = location
        }

        private fun isBetterLocation(location: Location, currentBestLocation: Location): Boolean {
            if (currentBestLocation == null) { // A new location is always better than no location
                return true
            }
            // Check whether the new location fix is newer or older
            val timeDelta = location.time - currentBestLocation.time
            val isSignificantlyNewer: Boolean = timeDelta > TWO_MINUTES
            val isSignificantlyOlder: Boolean = timeDelta < -TWO_MINUTES
            val isNewer = timeDelta > 0
            // If it's been more than two minutes since the current location, use the new location
// because the user has likely moved
            if (isSignificantlyNewer) {
                return true
                // If the new location is more than two minutes older, it must be worse
            } else if (isSignificantlyOlder) {
                return false
            }
            // Check whether the new location fix is more or less accurate
            val accuracyDelta = (location.accuracy - currentBestLocation.accuracy).toInt()
            val isLessAccurate = accuracyDelta > 0
            val isMoreAccurate = accuracyDelta < 0
            val isSignificantlyLessAccurate = accuracyDelta > 200
            // Check if the old and new location are from the same provider
            val isFromSameProvider: Boolean = isSameProvider(
                location.provider,
                currentBestLocation.provider
            )
            // Determine location quality using a combination of timeliness and accuracy
            if (isMoreAccurate) {
                return true
            } else if (isNewer && !isLessAccurate) {
                return true
            } else if (isNewer && !isSignificantlyLessAccurate && isFromSameProvider) {
                return true
            }
            return false
        }

        override fun onStatusChanged(
            provider: String,
            status: Int,
            extras: Bundle
        ) {
        }

        override fun onProviderDisabled(provider: String) {
            //  Toast.makeText(this, "Gps Disabled", Toast.LENGTH_SHORT).show()
        }

        override fun onProviderEnabled(provider: String) {
            //Toast.makeText(this, "Gps Enabled", Toast.LENGTH_SHORT).show()
        }
    }

    private fun stopForegroundService() {
        // Stop foreground service and remove the notification.
        stopForeground(true)
        // Stop the foreground service.
        stopSelf()
        if (locationManager != null)
            locationManager!!.removeUpdates(listener!!)
    }

    private fun startForegroundService(notification: Notification) {
        startForeground(notificationId, notification)

    }

    private fun SaveLocationToLocalDB(loc: LocationTable) {
        CoroutineScope(Dispatchers.IO).launch {
            MyAppDatabase.getInstance(applicationContext).getLocationMaster().insert(loc)
            withContext(Dispatchers.Main) {
                Log.e("SaveLocationToLocalDB", "success")
            }
        }

    }


    fun locationServiceNotification(activity: Context): Notification? {

        val builder = NotificationCompat.Builder(activity, "my_service")
        val bigTextStyle = NotificationCompat.BigTextStyle()
        bigTextStyle.setBigContentTitle(" Location Service Updating")
        builder.setContentTitle("App is running.")
        builder.setStyle(bigTextStyle)
        builder.setWhen(System.currentTimeMillis())
        builder.setSmallIcon(R.mipmap.ic_launcher_round)
        builder.priority = Notification.PRIORITY_LOW
        builder.setSound(null)
        builder.setAutoCancel(false)
        builder.setVibrate(null)
        builder.setOngoing(true)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationManager =
                activity.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            val notificationChannel = NotificationChannel(
                "my_service",
                "app Service",
                NotificationManager.IMPORTANCE_LOW
            )
            notificationChannel.setSound(null, null)
            notificationChannel.setShowBadge(false)
            notificationManager?.createNotificationChannel(notificationChannel)
        }
        // Build the notification.
        val notification = builder.build()
        return notification
    }

}
