package com.example.googlemapapp

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.location.FusedLocationProviderApi
import com.google.android.gms.location.LocationListener
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng


private val TAG = "GOOGLE_MAP"






class MainActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private lateinit var mApiClient: GoogleApiClient


    private lateinit var mProviderApi: FusedLocationProviderApi


    private lateinit var mLocationListener: LocationListener


    private val permissions = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION
    )


    private lateinit var googlemap: GoogleMap



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(permissions, 1)
        }
        else {
            init()
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

        val mapCallback = GoogleMapReadyCallback()

        val mapFragment = supportFragmentManager.findFragmentById(R.id.googleMapFragment) as SupportMapFragment
        mapFragment.getMapAsync(mapCallback)

        mApiClient.connect()
    }


    private fun permissionCheck(): Boolean {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if(checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_DENIED) {
                return false
            }
        }
        return true
    }



    override fun onStop() {
        super.onStop()
        googleMapDisconnect()
    }



    private fun googleMapConnect() {
        if(!mApiClient.isConnected) {
            mApiClient.connect()
        }
    }



    private fun googleMapDisconnect() {
        if(mApiClient.isConnected) {
            mProviderApi.removeLocationUpdates(mApiClient, mLocationListener)
            mApiClient.disconnect()
        }
        Log.d(TAG, "googleMapDisconnect")
    }



    private fun makeToast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


    /*
    사용자가 MyLocationButton 을 클릭할 때 지도가 움직을 수 있다.
    이로 인하여 setOnCameraMoveListener 가 호출 된다.
    첫 위치 정보를 수신할 때까지 true 값을 유지하여 setOnCameraMoveListener 호출 시 바로 리턴 시킨다.
    */
    private var isClickMyLocationButton = false



    // 구글 맵 프레그먼트 사용 가능
    inner class GoogleMapReadyCallback: OnMapReadyCallback {
        override fun onMapReady(map: GoogleMap?) {
            googlemap = map!!

            googlemap.setOnCircleClickListener {
                makeToast("setOnCircleClickListener")
            }

            googlemap.setOnInfoWindowCloseListener {
                makeToast("setOnInfoWindowCloseListener")
            }

            googlemap.setOnGroundOverlayClickListener {
                makeToast("setOnGroundOverlayClickListener")
            }

            googlemap.setOnMarkerClickListener {
                makeToast("setOnMarkerClickListener")
                return@setOnMarkerClickListener false
            }

            /*
                지도 우측 상단의 버튼
                googlemap.isMyLocationEnabled = true 이면 활성
            */
            googlemap.setOnMyLocationButtonClickListener {
                isClickMyLocationButton = true
                Log.d(TAG, "setOnMyLocationButtonClickListener begin")
                googleMapConnect()
                return@setOnMyLocationButtonClickListener false
            }

            /*
              사용자가 지도를 이동하였을 때...
              googlemap.moveCamera() 호출로 인한 호출은 발생하지 않는다.
            */
            googlemap.setOnCameraMoveListener {
                if(isClickMyLocationButton) {
                    return@setOnCameraMoveListener
                }
                Log.d(TAG, "setOnCameraMoveListener begin")
                googleMapDisconnect()
            }

            googlemap.setOnCameraMoveCanceledListener {
                Log.d(TAG, "setOnCameraMoveCanceledListener begin")
            }

            /*
             지도 중심 위치 변경이 완료되었을 때...
             googlemap.moveCamera() 호출하여 제공 받은 위치로 이동할 때도 호출 된다.
            */
            googlemap.setOnCameraIdleListener {
                Log.d(TAG, "setOnCameraIdleListener begin")
            }
        }
    }



    // GoogleApiClient.ConnectionCallbacks 구현
    // 사용 불가능 상태가 되었을 때
    override fun onConnectionSuspended(p0: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    // GoogleApiClient.OnConnectionFailedListener 구현
    // 위치 정보제공자를 얻지 못할 때
    override fun onConnectionFailed(p0: ConnectionResult) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }



    // GoogleApiClient.ConnectionCallbacks 구현
    // 위치 정보 제공자가 사용 가능 상태가 되었을 때
    @SuppressLint("MissingPermission")
    override fun onConnected(p0: Bundle?) {
        Log.d(TAG, "onConnected begin")
        if(!permissionCheck()) {
            return
        }
        // 정상적으로 구글멥에 연결 되었다면 MyLocation 버튼 활성 여부 및
        // 지도 유형 설정.
        googlemap.isMyLocationEnabled = true
        googlemap.mapType = GoogleMap.MAP_TYPE_NORMAL
//        googlemap.mapType = GoogleMap.MAP_TYPE_HYBRID
//        googlemap.mapType = GoogleMap.MAP_TYPE_SATELLITE
        requestLocationUpdates()
        Log.d(TAG, "isClickMyLocationButton => ${isClickMyLocationButton}")
        Log.d(TAG, "onConnected end")
    }



    @SuppressLint("MissingPermission")
    private fun requestLocationUpdates() {
        Log.d(TAG, "requestLocationUpdates begin")
        Log.d(TAG, "isClickMyLocationButton => ${isClickMyLocationButton}")

        val locationRequest = LocationRequest.create()
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
        locationRequest.setInterval(3000)
        mProviderApi.requestLocationUpdates(mApiClient, locationRequest, mLocationListener)

        Log.d(TAG, "isClickMyLocationButton => ${isClickMyLocationButton}")
        Log.d(TAG, "requestLocationUpdates end")
    }


    // com.google.android.gms.location.LocationListener 주의
    inner class MyLocationListener: LocationListener {
        override fun onLocationChanged(p0: Location?) {
            Log.d(TAG, "MyLocationListener.onLocationChanged begin")
            Log.d(TAG, "isClickMyLocationButton => ${isClickMyLocationButton}")
            setCurrentLocaion(p0!!)
            isClickMyLocationButton = false
            Log.d(TAG, "isClickMyLocationButton => ${isClickMyLocationButton}")
            Log.d(TAG, "MyLocationListener.onLocationChanged end")
            // onLocationChanged 가 끝나고
            // 위치가 변경되었다면 googlemap.setOnCameraIdleListener 가 동작 함.
        }
    }


    private fun setCurrentLocaion(location: Location) {
        Log.d(TAG, "setCurrentLocaion begin")
        val prov = location.provider
        val lat = location.latitude // 위도
        val lng = location.longitude // 경도
        val acc = location.accuracy // 정확도
        val bear = location.bearing // 방위
        val speed = location.speed // 속도
        val alt = location.altitude // 고도

        Log.d(TAG, "현재 = 제공: ${prov}, 위도: ${lat}, 경도: ${lng}, 정확도: ${acc}, 방위: ${bear}, 속도: ${speed},  고도: ${alt}")

        // 위치 정보를 받아 구글 맵에 현재 위치 표시
        val latlng = LatLng(location.latitude, location.longitude)
        val cp = CameraPosition.Builder().target(latlng).zoom(16f).build()
        googlemap.moveCamera(CameraUpdateFactory.newCameraPosition(cp))

        Log.d(TAG, "setCurrentLocaion end")
    }

}
