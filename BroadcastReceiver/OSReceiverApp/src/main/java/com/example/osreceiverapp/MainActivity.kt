package com.example.osreceiverapp

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle


/*
    시스템 메시지
    - 안드로이드에서는 단말기에서 사건이 발생했을 경우 각 사건만다 정해 놓은 이름으로 시스템 메시지를 발생시킨다.
    - 이 메시지와 동일한 이름으로 설정되어 있는 Broad Cast Receiver를 찾아 개발자가 만든 코드를 동작시킬 수 있다.
    - 안드로이드 8.0 부터는 사용할 수 있는 시스템 메시지의 수가 줄어들었다.

    https://developer.android.com/guide/components/broadcast-execeptions.html
 */
class MainActivity : AppCompatActivity() {

    private val permissionList = arrayOf(
        Manifest.permission.READ_PHONE_STATE,
        Manifest.permission.RECEIVE_SMS
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermisstion()
    }


    private fun checkPermisstion() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }
        val deniedCount =
            permissionList
                .map { checkCallingOrSelfPermission(it) }
                .filter { it == PackageManager.PERMISSION_DENIED }
                .count()
        if(deniedCount > 0) {
            requestPermissions(permissionList, 0)
        }
    }


}
