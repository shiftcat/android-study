package com.example.gps

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


    private lateinit var locManager: LocationManager

    private val locationListener = MyLocationListener()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1)
        }
        else {
            init()
        }


        button.setOnClickListener {
            getMyLocation()
        }
    }



    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        val deniedCount =
            grantResults
                    .filter {
                        PackageManager.PERMISSION_DENIED == it
                    }
                    .count()
        if(deniedCount > 0) {
            return
        }
        init()
    }



    private fun init() {
        locManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
    }


    override fun onStop() {
        super.onStop()
        locManager.removeUpdates(locationListener)
    }


    private fun getMyLocation() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        val allProviders = locManager.allProviders.joinToString(", ")
        val enableProviders = locManager.getProviders(true).joinToString(", ")
        textViewAllProviders.text = allProviders
        textViewEnableProviders.text = enableProviders

        // 이전에 마지막으로 측정된 위치 정보
        val gpsLoc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
        val netLoc = locManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER)

        if(gpsLoc != null) {
            setPreviousLocaion(gpsLoc)
        }
        else if(netLoc != null) {
            setPreviousLocaion(netLoc)
        }


        /*
        위치 정보 제공자를 명시적으로 이용하는 방법
        // GPS 측정이 가능하다면...
        if(locManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 10f, locationListener)
        }
        // 네트워크 측정이 가능하다면...
        else if(locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)) {
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 1000, 10f, locationListener)
        }
        */

        // 조건을 명시하여 조건에 가장 최적의 위치 정보 제공자를 이용하는 방법.
        val criteria = Criteria()
        criteria.accuracy = Criteria.ACCURACY_FINE
        criteria.isAltitudeRequired = false
        criteria.isBearingRequired = false
        criteria.isSpeedRequired = false
        criteria.isCostAllowed = true
        criteria.powerRequirement = Criteria.POWER_LOW
        val provider = locManager.getBestProvider(criteria, true)
        Log.d("LOCATION", "Best Provider: ${provider}")
        locManager.requestLocationUpdates(provider, 1000, 10f, locationListener)

    }


    private fun setPreviousLocaion(location: Location) {
        val prov = location.provider
        val lat = location.latitude // 위도
        val lng = location.longitude // 경도
        val acc = location.accuracy // 정확도
        val bear = location.bearing // 방위
        val speed = location.speed // 속도
        val alt = location.altitude // 고도

        Log.d("LOCATION", "이전 = 제공: ${prov}, 위도: ${lat}, 경도: ${lng}, 정확도: ${acc}, 방위: ${bear}, 속도: ${speed},  고도: ${alt}")

        textViewPreviousLoc.text = "위도: ${lat}, 경도: ${lng}"
    }


    private fun setCurrentLocaion(location: Location) {
        val prov = location.provider
        val lat = location.latitude // 위도
        val lng = location.longitude // 경도
        val acc = location.accuracy // 정확도
        val bear = location.bearing // 방위
        val speed = location.speed // 속도
        val alt = location.altitude // 고도

        Log.d("LOCATION", "현재 = 제공: ${prov}, 위도: ${lat}, 경도: ${lng}, 정확도: ${acc}, 방위: ${bear}, 속도: ${speed},  고도: ${alt}")


        textViewCurrentLoc.text = "위도: ${lat}, 경도: ${lng}"
        textViewAccuracy.text = "정확도: ${acc}"
        textViewTime.text = location.time.toString()
    }



    inner class MyLocationListener: LocationListener {
        // 측정 성공
        override fun onLocationChanged(location: Location?) {
            setCurrentLocaion(location!!)
        }

        override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
            // provider의 상태가 변경될때마다 호출
        }

        override fun onProviderEnabled(provider: String?) {
            // provider가 사용 가능한 상태가 되는 순간 호출
        }

        override fun onProviderDisabled(provider: String?) {
            // provider가 사용 불가능한 상황이 되는 순간 호출
            locManager.removeUpdates(this)
        }

    }


}
