package com.example.servicebasicapp

import android.app.IntentService
import android.content.Intent
import android.os.SystemClock
import android.util.Log


class MyIntentService : IntentService("MyIntentService") {

    val LOG_TAG = "ServiceBasicApp"

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "${javaClass.name} onCreate")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "${javaClass.name} onBind")
        return super.onStartCommand(intent, flags, startId)
    }


    // 별도의 쓰래드로 동작 한다.
    override fun onHandleIntent(intent: Intent?) {
        Log.d(LOG_TAG, "${javaClass.name} onHandleIntent")
        var idx = 0
        while (idx < 10) {
            SystemClock.sleep(1000)
            var time = System.currentTimeMillis()
            Log.d(LOG_TAG, "Intent Service Runnig: ${time}")
            idx++
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "${javaClass.name} onDestroy")
    }

}
