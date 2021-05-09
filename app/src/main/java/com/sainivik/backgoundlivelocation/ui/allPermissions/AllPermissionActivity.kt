package com.sainivik.backgoundlivelocation.ui.allPermissions

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.databinding.ActivityAllPermissionBinding
import com.sainivik.backgoundlivelocation.ui.base.BaseActivity
import com.sainivik.backgoundlivelocation.util.MiscUtil
import com.sainivik.backgoundlivelocation.util.PermissionHelper
import com.sainivik.backgoundlivelocation.util.PermissionHelper.Permissions.Companion.MY_IGNORE_OPTIMIZATION_REQUEST
import com.sainivik.backgoundlivelocation.util.PermissionHelper.Permissions.Companion.MY_PERMISSIONS_REQUEST_LOCATION


class AllPermissionActivity : BaseActivity() {

    lateinit var binding: ActivityAllPermissionBinding
    var from = ""
    override fun setBinding() {
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_all_permission
        )
        /*  binding.isLocationEnabled = PermissionHelper.checkLocationPermission(this)
          binding.isGPSEnabled = checkGPSPermission()
          binding.isNotificationEnabled = MiscUtil.checkNotificationIsEnabled(this)*/
        getIntentData()
        binding.showIgnoreBatteryOptimization = Build.VERSION.SDK_INT >= 23
        setListener()
    }

    override fun onResume() {
        super.onResume()
        binding.isLocationEnabled = PermissionHelper.checkLocationPermission(this)
        binding.isGPSEnabled = checkGPSPermission()
        binding.swGPS.isChecked = checkGPSPermission()
        binding.allPermissionEnabled = MiscUtil.checkAllRequiredPermission(this)
        binding.ignoreBatteryOptimization = MiscUtil.checkBatteryOptimization(this)
        binding.swBatteryOptimization.isChecked = MiscUtil.checkBatteryOptimization(this)
    }

    private fun checkGPSPermission(): Boolean {
        val locationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
    }


    private fun getIntentData() {


    }


    override fun attachViewModel() {

    }

    override fun onBackPressed() {
        super.onBackPressed()

    }


    private fun setListener() {

        binding.tvNext.setOnClickListener {

            if (MiscUtil.checkAllRequiredPermission(
                    this
                )
            ) {
                finish()
            } else {
                Toast.makeText(this, "Please allow all permission.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.swLocation.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && !binding.isLocationEnabled!!) {

                PermissionHelper.requestLocationPermission(this@AllPermissionActivity)
            }

        }
        binding.swGPS.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked && !binding.isGPSEnabled!!) {
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }

        }

        binding.swBatteryOptimization.setOnCheckedChangeListener { buttonView, isChecked ->

            if (isChecked && !binding.ignoreBatteryOptimization!!) {
                if (Build.VERSION.SDK_INT >= 23) {
                    startActivity(
                        Intent(
                            Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                            Uri.parse("package:$packageName")
                        )
                    )
                }
            }

        }


    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == MY_PERMISSIONS_REQUEST_LOCATION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                binding.isLocationEnabled = true
            } else {
                binding.swLocation.isChecked = false


            }
            binding.allPermissionEnabled = MiscUtil.checkAllRequiredPermission(this)
        } else if (requestCode == MY_IGNORE_OPTIMIZATION_REQUEST) {
            if (Build.VERSION.SDK_INT >= 23) {
                val pm =
                    getSystemService(Context.POWER_SERVICE) as PowerManager
                val isIgnoringBatteryOptimizations = pm.isIgnoringBatteryOptimizations(packageName)
                binding.ignoreBatteryOptimization = isIgnoringBatteryOptimizations
                binding.swBatteryOptimization.isChecked = isIgnoringBatteryOptimizations
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }


}
