package com.example.bootingwithstartserviceapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast


/**
 * 부팅 완료 후
 */
class MyReceiver : BroadcastReceiver() {

    val LOG_TAG = "BootingWithStartService"

    override fun onReceive(context: Context, intent: Intent) {
        Log.d(LOG_TAG, "${javaClass.name} onReceive()")

        when(intent.action) {
            Intent.ACTION_BOOT_COMPLETED -> {
                Toast.makeText(context, "Booting complete", Toast.LENGTH_LONG).show()
                val myServiceIntent = Intent(context, MyService::class.java)
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    context.startForegroundService(myServiceIntent)
                }
                else {
                    context.startService(myServiceIntent)
                }
            }
        }
    }
}
