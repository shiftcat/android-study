package com.example.servicebasicapp

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.IBinder
import android.os.SystemClock
import android.support.v4.app.NotificationCompat
import android.util.Log
import java.lang.UnsupportedOperationException
import java.util.concurrent.Executors

class ForgroundService : Service() {

    val LOG_TAG = "ServiceBasicApp"

    override fun onCreate() {
        super.onCreate()
        Log.d(LOG_TAG, "${javaClass.name} onCreate()")
    }

    override fun onBind(intent: Intent): IBinder? {
        Log.d(LOG_TAG, "${javaClass.name} onBind()")
        throw UnsupportedOperationException("Not yet implemented")
    }


    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "${javaClass.name} onStartCommand()")

        var builder:NotificationCompat.Builder? = null

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel("test1", "Service", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, "test1")
        }
        else {
            builder = NotificationCompat.Builder(this)
        }
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        builder.setContentTitle("서비스 시작")
        builder.setContentText("서비스를 시작 합니다.")

        val notification = builder.build()
        startForeground(10, notification)

        execute()

        return super.onStartCommand(intent, flags, startId)
    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "${javaClass.name} onDestroy()")
    }


    private fun execute() {
        val executors = Executors.newSingleThreadExecutor()
        executors.execute {
            Log.d(LOG_TAG, "Thread start")
            var idx = 0
            while (idx < 10) {
                SystemClock.sleep(1000)
                var time = System.currentTimeMillis()
                Log.d("ServiceBasicApp", "Forground Service Running: ${time}")
                idx++
            }
            Log.d(LOG_TAG, "Thread complete!!")
        }
        executors.shutdown()
    }


}
