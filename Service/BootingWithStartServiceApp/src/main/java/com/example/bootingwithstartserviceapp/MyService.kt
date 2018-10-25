package com.example.bootingwithstartserviceapp

import android.app.Notification
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
import android.widget.Toast
import java.util.concurrent.Executors

class MyService : Service() {


    val LOG_TAG = "BootingWithStartService"


    override fun onBind(intent: Intent): IBinder {
        TODO("Return the communication channel to the service.")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.d(LOG_TAG, "${javaClass.name} onStartCommand()")
        Toast.makeText(this, "Start with service start", Toast.LENGTH_LONG).show()

        var builder: NotificationCompat.Builder? = null

        var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        val channelId = "test1"

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var channel = NotificationChannel(channelId, "MyServiceChannel", NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, channelId)
        }
        else {
            builder = NotificationCompat.Builder(this)
        }
        builder.setSmallIcon(android.R.drawable.ic_menu_search)
        builder.setContentTitle("서비스 시작")
        builder.setContentText("서비스를 시작 합니다.")
        builder.setAutoCancel(true)

        var notification = builder.build()

        // startForegroundService()에 의해 실행되는 경우 빠른 시간내 호출 되어야 함.
        startForeground(10, notification)

        /*
        Notification을 숨기고 서비스 실행하는 꼼수...
        이 코드는 8.0에서는 정상 작동 되지만, 안드로이 9.0 에서는 정상 작동하지 않는다.
        startForeground(10, Notification())
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(10, notification)
        manager.cancel(10)
        */

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
            while (idx < 60) {
                SystemClock.sleep(1000)
                var time = System.currentTimeMillis()
                Log.d(LOG_TAG, "Forground Service Running: ${time}")
                idx++
            }
            Log.d(LOG_TAG, "Thread complete!!")
        }
        executors.shutdown()
    }
}
