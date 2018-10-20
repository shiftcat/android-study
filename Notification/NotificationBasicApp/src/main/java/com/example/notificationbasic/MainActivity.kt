package com.example.notificationbasic

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*

/*
    Notification
    - Notification은 애플리케이션과 별도로 관리되는 메시지이다.
    - Notification 메시지를 OS에게 요청하면 OS는 알림 창 영역에 알림 메시지를 표시한다.
    - 화면을 가지지 않는 실행단위에서 메시지를 표시할 때 주로 사용한다.

    특징
    - 사용자가 메시지를 확인하거나 제거하기 전까지 메시지를 유지되어 있다.
    - 메시지를 터치하면 지정된 Activity가 실행되어 애플리케이션 실행을 유도할 수 있다.


    Notification Channel
    - 안드로이드 8.0 부터 새롭게 추가된 기능으로 사용자가 애플리케이션의 알림 메시지를 출력하지 않도록 설정하면
      모든 메시지가 출력되지 않는다.
    - Notification Channel을 이용하면 알림 메시지를 채널이라는 그룹으로 묶을 수 있으며
      같은 채널 메시지에 대한 설정을 따로 할 수 있게 된다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*
        8.0 이전 방식의 Notification 방식으로 8.0 부터 작동되지 않는다.
         */
//        button1.setOnClickListener {
//            var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
//            var builder = NotificationCompat.Builder(this)
//            builder.setTicker("Ticker")
//                .setSmallIcon(android.R.drawable.ic_menu_search)
//                .setLargeIcon(bitmap)
//                .setAutoCancel(true)
//                .setContentTitle("Content Title")
//                .setContentText("Content Text")
//
//            var notication = builder.build()
//            var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
//            mng.notify(10, notication)
//        }

        button1.setOnClickListener {
            var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            var builder = getNotificationBuilder1("channel1", "첫 번째 채널")
            builder.setTicker("Ticker")
                .setSmallIcon(android.R.drawable.ic_menu_search)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentTitle("Content Title")
                .setContentText("Content Text")

            var notication = builder.build()
            var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mng.notify(10, notication)
        }


        button2.setOnClickListener {
            var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            var builder = getNotificationBuilder1("channel1", "첫 번째 채널")
            builder.setTicker("Ticker")
                .setSmallIcon(android.R.drawable.ic_menu_search)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentTitle("Content Title 2")
                .setContentText("Content Text 2")

            var notication = builder.build()
            var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mng.notify(20, notication)
        }

        button3.setOnClickListener {
            var bitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher)
            var builder = getNotificationBuilder1("channel2", "두 번째 채널")
            builder.setTicker("Ticker")
                .setSmallIcon(android.R.drawable.ic_menu_search)
                .setLargeIcon(bitmap)
                .setAutoCancel(true)
                .setContentTitle("Content Title 3")
                .setContentText("Content Text 3")

            var notication = builder.build()
            var mng = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mng.notify(30, notication)
        }
    }


    fun getNotificationBuilder1(id: String, name: String): NotificationCompat.Builder {
        var builder: NotificationCompat.Builder? = null

        // Oreo 8.0 이후 버전 체크하여 분기
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            var channel = NotificationChannel(id, name, NotificationManager.IMPORTANCE_HIGH)
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(true)
            manager.createNotificationChannel(channel)

            builder = NotificationCompat.Builder(this, id)

        }
        else {
            // 8.0 이전 방식
            builder = NotificationCompat.Builder(this, id)
        }

        return builder
    }
}
