package com.example.fusedlocationapp

import android.Manifest
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import kotlinx.android.synthetic.main.activity_main.*


/*
    위치 정보 제공자를 GPS, Wifi, Cell, Sensor로 정의하고 각각의 배터리 소모, 정확도, 서비스 영역 등을 고려해서 결정해야 하는데,
    이 작업을 대행하고자 하는 목적이 Fused Location API이다.

    다음 의존성 관계를 설정해야 한다.

    implementation 'com.google.android.gms:play-services:11.2.0'
 */
class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {


    private lateinit var mApiClient: GoogleApiClient


    private lateinit var mProviderApi: FusedLocationProviderApi


    private lateinit var mLocationListener: LocationListener


    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )


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
            if(!mApiClient.isConnected) {
                mApiClient.connect()
            }
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
        mApiClient = GoogleApiClient.Builder(this)
            .addApi(LocationServices.API)
            .addConnectionCallbacks(this)
            .addOnConnectionFailedListener(this)
            .build()

        mProviderApi = LocationServices.FusedLocationApi

        mLocationListener = MyLocationListener()
    }


//    override fun onResume() {
//        super.onResume()
//        mApiClient.connect()
//    }


    override fun onStop() {
        super.onStop()
        if(mApiClient.isConnected) {
            mProviderApi.removeLocationUpdates(mApiClient, mLocationListener)
            mApiClient.disconnect()
        }
    }


    // com.google.android.gms.location.LocationListener 주의
    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(p0: Location?) {
            setCurrentLocaion(p0!!)
        }
    }



    // GoogleApiClient.ConnectionCallbacks 구현
    // 위치 정보 제공자가 사용 가능 상태가 되었을 때
    override fun onConnected(p0: Bundle?) {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return
            }
            if(checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return
            }
        }

        val location = mProviderApi.getLastLocation(mApiClient)
        if(location != null) {
            setPreviousLocaion(location)
        }


        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(3000)
        mProviderApi.requestLocationUpdates(mApiClient, locationRequest, mLocationListener)
    }



    // GoogleApiClient.ConnectionCallbacks 구현
    // 사용 불가능 상태가 되었을 때
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    // GoogleApiClient.OnConnectionFailedListener 구현
    // 위치 정보제곡자를 얻지 못할 때
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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




}
