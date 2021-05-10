package com.sainivik.backgoundlivelocation.ui.mainactivity

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.databinding.ActivityMainBinding
import com.sainivik.backgoundlivelocation.model.LocationTable
import com.sainivik.backgoundlivelocation.services.LiveLocationService
import com.sainivik.backgoundlivelocation.ui.allPermissions.AllPermissionActivity
import com.sainivik.backgoundlivelocation.ui.base.BaseActivity
import com.sainivik.backgoundlivelocation.ui.logActivity.LogsActivity
import com.sainivik.backgoundlivelocation.ui.mapActivity.MapActivity
import com.sainivik.backgoundlivelocation.util.MiscUtil

class MainActivity : BaseActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var viewModel: MainActivityViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        setListener()
    }

    override fun attachViewModel() {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        /*checking service is running or not*/
        binding.isServiceRunning =
            isMyServiceRunning(LiveLocationService::class.java, this@MainActivity)
    }

    /*setting click listener to start and stop service*/
    private fun setListener() {
        binding.btnStartTracking.setOnClickListener {
            if (MiscUtil.checkAllRequiredPermission(this)) {
                val intent = Intent(this, LiveLocationService::class.java)
                intent.action = LiveLocationService.ACTION_START_FOREGROUND_SERVICE
                startService(intent)
                binding.isServiceRunning = true
                setStartAndStopTime(true)
            } else {
                startActivity(Intent(this@MainActivity, AllPermissionActivity::class.java))

            }


        }
        binding.btnStopTracking.setOnClickListener {
            val intent = Intent(this, LiveLocationService::class.java)
            intent.action = LiveLocationService.ACTION_STOP_FOREGROUND_SERVICE
            startService(intent)
            binding.isServiceRunning = false
            setStartAndStopTime(false)
            var mapIntent = Intent(this@MainActivity, MapActivity::class.java)
            startActivity(mapIntent)

        }

        binding.btnShowLogs.setOnClickListener {

            startActivity(Intent(this@MainActivity, LogsActivity::class.java))
        }
    }

    /*save start and stop time in DB*/
    private fun setStartAndStopTime(isStart: Boolean) {
        var type = 0
        type = if (isStart) {
            2
        } else {
            3
        }
        var time = System.currentTimeMillis()
        val location1 = LocationTable(
            System.currentTimeMillis(),
            0.0,
            0.0,
            System.currentTimeMillis(),
            0.0,
            0.0,
            0.0,
            time, time, type


        )
        viewModel.saveLocationToLocalDB(location1, this@MainActivity)
    }

    /*method to check is service running or not*/
    private fun isMyServiceRunning(serviceClass: Class<*>, context: Context): Boolean {
        val manager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        for (service in manager.getRunningServices(Int.MAX_VALUE)) {
            if (serviceClass.name == service.service.className) {
                return true
            }
        }
        return false
    }
}