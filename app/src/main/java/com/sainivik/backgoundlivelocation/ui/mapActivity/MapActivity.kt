package com.sainivik.backgoundlivelocation.ui.mapActivity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.PersistableBundle
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.sainivik.backgoundlivelocation.R
import com.sainivik.backgoundlivelocation.databinding.ActivityMapBinding
import com.sainivik.backgoundlivelocation.model.LocationTable
import com.sainivik.backgoundlivelocation.ui.base.BaseActivity


class MapActivity : BaseActivity(), OnMapReadyCallback {
    lateinit var binding: ActivityMapBinding
    lateinit var viewModel: MapActivityViewModel
    lateinit var mMap: GoogleMap
    var list: ArrayList<LocationTable> = ArrayList<LocationTable>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)

    }

    override fun setBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_map)
        getData()
        initMap()
    }

    private fun initMap() {
        var fm = supportFragmentManager
        val mapFragment = fm.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

    }

    /*getting data from intent*/
    private fun getData() {
        if (intent != null) {
            if (intent.getSerializableExtra("data") != null) {
                list = intent.getSerializableExtra("data") as ArrayList<LocationTable>
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return
        }
        mMap.isMyLocationEnabled = true
        var marker = LatLng(0.0, 0.0)
        if (list != null) {
            for ((pos, locationTable) in list.withIndex()) {
                marker = LatLng(locationTable.Lat, locationTable.Lng)
                mMap.addMarker(MarkerOptions().position(marker))
                if (pos == 0) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(marker))
                    mMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            marker, 12.0f
                        )
                    )

                }

            }
        }
    }

    override fun attachViewModel() {
        viewModel = ViewModelProvider(this).get(MapActivityViewModel::class.java)

    }


}