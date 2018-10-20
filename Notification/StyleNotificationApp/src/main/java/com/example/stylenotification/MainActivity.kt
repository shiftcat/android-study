package com.example.stylenotification

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.support.v4.app.NotificationCompat
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

/*
    Style Notification
    - 안드로이드 4.1에 새롭게 추가된 부분으로 4.0 이하 버전에서는 기본 알림 메시지로 표시된다.
    - 안드로이드 4.1 이후 부터는 알림 메시지를 접었다 펼쳤다 하면서 부가 정보를 표시할 수 있다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var builder = getNotificationBuilder1("style", "style notification")
                .setContentTitle("Big Picture")
                .setContentText("Big Picture Notification")
                .setSmallIcon(android.R.drawable.ic_media_next)

            var big = NotificationCompat.BigPictureStyle(builder)
            var bitmap = BitmapFactory.decodeResource(resources, R.drawable.android_pack)
            big.bigPicture(bitmap)
            big.setBigContentTitle("Big Cotent Title")
                .setSummaryText("Summary Text")

            var notification = builder.build()
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(10, notification)
        }


        button2.setOnClickListener {
            var builder = getNotificationBuilder1("style", "style notification")
                .setContentTitle("Big Text")
                .setContentText("Big Text Notification")
                .setSmallIcon(android.R.drawable.ic_media_next)

            var big = NotificationCompat.BigTextStyle(builder)
            big.setSummaryText("Summary Text")
                .setBigContentTitle("Big content title")
                .bigText("""
비전공자를 위한 게임 개발 2018 ver.
프로그래밍은 1도 모르겠고 일단 게임은 만들어 보고 싶다면 마구 떠오르는 아이디어를 직접 구현해보세요.
기초부터 배포까지 문과생도, 디자이너도 얼마든지 시작할 수 있어요!
                """.trimIndent())

            var notification = builder.build()
            var manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.notify(20, notification)
        }

        button3.setOnClickListener {
            var builder = getNotificationBuilder1("style", "style notification")
                .setContentTitle("InBox")
                .setContentText("InBox Notification")
                .setSmallIcon(android.R.drawable.ic_media_next)

            var inbox = NotificationCompat.InboxStyle(builder)
            inbox.setSummaryText("Summary Text")
                .setBigContentTitle("Big content title")
                .addLine("비전공자를 위한 게임 개발 2018 ver.")
                .addLine("프로그래밍은 1도 모르겠고 일단 게임은 만들어 보고 싶다면 마구 떠오르는 아이디어를 직접 구현해보세요.")
                .addLine("기초부터 배포까지 문과생도, 디자이너도 얼마든지 시작할 수 있어요!")

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
