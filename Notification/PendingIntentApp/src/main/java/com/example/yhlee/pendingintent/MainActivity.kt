package com.example.yhlee.pendingintent

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import kotlinx.android.synthetic.main.activity_main.*


/*
    Pending Intent
    - Notification 메시지를 통해 애플리케이션의 Activity를 실행할 수 있는데 이 때 사용하는 것이 Pending Intent 이다.
    - Pending Intent 를 통헤 실행되는 Activity로 데이터를 전달할 수도 있다.

 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        button1.setOnClickListener {
            var intent1 = Intent(this, TestActivity1::class.java)
            intent1.putExtra("data1", "문자열 데이터 1")
            intent1.putExtra("data2", 100)

            // action에 대한 설정 - 반드시 필요한 것은 아니다.
            var intent2 = Intent(this, TestActivity2::class.java)
            intent2.putExtra("data1", "액티비티 2")
            intent2.putExtra("data2", 200)


            // PendingIntent.FLAG_UPDATE_CURRENT 반드시 필요
            var pending1 = PendingIntent.getActivity(this, 10, intent1, PendingIntent.FLAG_UPDATE_CURRENT)
            // action에 대한 설정 - 반드시 필요한 것은 아니다.
            var pending2 = PendingIntent.getActivity(this, 100, intent2, PendingIntent.FLAG_UPDATE_CURRENT)

            // Notification 에 action 추가를 위한 설정 - 반드시 필요한 것은 아니다.
            var builder2 = NotificationCompat.Action.Builder(android.R.drawable.ic_menu_compass, "Action1", pending2)
            var action2 = builder2.build()

            var builder = getNotificationBuilder1("peding", "pending intent")
            builder.setContentTitle("notification 1")
                .setContentText("알림 메시지 1 입니다.")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentIntent(pending1)
                .addAction(action2) // action 추가

            var notification = builder.build()
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(10, notification)
        }


        button2.setOnClickListener {
            var intent1 = Intent(this, TestActivity2::class.java)
            intent1.putExtra("data1", "문자열 데이터 2")
            intent1.putExtra("data2", 200)

            var pending1 = PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

            var builder = getNotificationBuilder1("peding", "pending intent")
            builder.setContentTitle("notification 2")
                .setContentText("알림 메시지 2 입니다.")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentIntent(pending1)

            var notification = builder.build()
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(20, notification)
        }


        // 버튼1과 동일한 Activity를 실행하는 경우
        // 같은 Activity를 실행하는 경우 PendingIntent.getActivity() 인자값 requestCode를 다르게 하여 실행해야 한다.
        button3.setOnClickListener {
            var intent1 = Intent(this, TestActivity1::class.java)
            intent1.putExtra("data1", "문자열 데이터 3")
            intent1.putExtra("data2", 300)

            // PendingIntent.FLAG_UPDATE_CURRENT 반드시 필요
            var pending1 = PendingIntent.getActivity(this, 30, intent1, PendingIntent.FLAG_UPDATE_CURRENT)

            var builder = getNotificationBuilder1("peding", "pending intent")
            builder.setContentTitle("notification 3")
                .setContentText("알림 메시지 3 입니다.")
                .setSmallIcon(android.R.drawable.ic_menu_camera)
                .setAutoCancel(true)
                .setContentIntent(pending1)

            var notification = builder.build()
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(30, notification)
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
