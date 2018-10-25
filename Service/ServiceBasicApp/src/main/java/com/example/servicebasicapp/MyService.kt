package com.example.servicebasicapp

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.SystemClock
import android.util.Log
import java.util.concurrent.Executors

class MyService : Service() {

    val LOG_TAG = "ServiceBasicApp"


    override fun onBind(intent: Intent?): IBinder? {
        Log.d(LOG_TAG, "MyService onBind")
        return null
    }


    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "${javaClass.name} onCreate")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "${javaClass.name} onStartCommand")

        val executors = Executors.newSingleThreadExecutor()

        executors
            .execute {
                Log.d(LOG_TAG, "Thread start.")
                var idx = 0
                while (idx < 10) {
                    SystemClock.sleep(1000)
                    var time = System.currentTimeMillis()
                    Log.d(LOG_TAG, "Service Running: ${time}")
                    idx++
                }
                Log.d(LOG_TAG, "Thread complete!!")
            }

        executors.shutdown()

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "${javaClass.name} onDestroy")
    }



}
