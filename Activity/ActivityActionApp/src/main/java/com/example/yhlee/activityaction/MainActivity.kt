package com.example.yhlee.activityaction

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    var permissionList = arrayOf(
        Manifest.permission.CALL_PHONE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var uri = Uri.parse("geo:37.243243,131.861601")
            var intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        button2.setOnClickListener {
            var uri = Uri.parse("http://developer.android.com")
            var intent = Intent(Intent.ACTION_VIEW, uri)
            startActivity(intent)
        }


        button3.setOnClickListener {
            var uri = Uri.parse("tel:03112345432")
            var intent = Intent(Intent.ACTION_DIAL, uri)
            startActivity(intent)
        }

        checkPermission()

        button4.setOnClickListener {
            var uri = Uri.parse("tel:03112345432")
            var intent = Intent(Intent.ACTION_CALL, uri)
            startActivity(intent)
        }
    }


    fun checkPermission() {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return
        }

        var deniedCnt = permissionList
                                .map { checkCallingOrSelfPermission(it) }
                                .filter { it == PackageManager.PERMISSION_DENIED }
                                .count()
        if (deniedCnt > 0) {
            requestPermissions(permissionList, 0)
        }
    }
}
