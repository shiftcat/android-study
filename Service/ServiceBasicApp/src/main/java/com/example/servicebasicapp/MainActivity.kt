package com.example.servicebasicapp

import android.content.Intent
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    서비스
    - 안드로이드 4대 구성요소 중 하나로 백그라운드 처리를 위해 제공되는 요소이다.
    - Activity는 화면을 가지고 있어 화면이 보이는 동안 동작하지만 Service는 화면을 가지고 있지 않아
      보이지 않는 동안에도 동작하는 것을 의미한다.

    Intent Service
    - IntentService는 Service 내부에서 Thread를 운영하고자 할 때 사용하는 서비스이다.

    Forground Service
    - 서비스는 기본적으로 백그라운드에서 운영되는 실행 요소로써 메모리가 부족해지거나 하면 안드로이드 OS에 의해 제거된다.
    - 이를 방지하고자 할때는 Forground Service로 만들어 사용하면 된다.

 */
class MainActivity : AppCompatActivity() {

    val LOG_TAG = "ServiceBasicApp"

    val myService: Intent by lazy {
        Intent(this, MyService::class.java)
    }

    val myIntentService: Intent by lazy {
        Intent(this, MyIntentService::class.java)
    }


    val forgroundService: Intent by lazy {
        Intent(this, ForgroundService::class.java)
    }

    var isMyServiceRunning = false

    var isMyIntentServiceRunning = false

    var isForgroundServiceRunning = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.text = if(isMyServiceRunning) "서비스 중지" else "서비스 시작"
        button1.setOnClickListener {
            if(isMyServiceRunning) {
                stopService(myService)
                isMyServiceRunning = false
            }
            else {
                startService(myService)
                isMyServiceRunning = true
            }
            button1.text = if(isMyServiceRunning) "서비스 중지" else "서비스 시작"
        }

        button2.text = if(isMyIntentServiceRunning) "인텐트 서비스 중지" else "인텐트 서비스 시작"
        button2.setOnClickListener {
            if(isMyIntentServiceRunning) {
                stopService(myIntentService)
                isMyIntentServiceRunning = false
            }
            else {
                startService(myIntentService)
                isMyIntentServiceRunning = true
            }
            button2.text = if(isMyIntentServiceRunning) "인텐트 서비스 중지" else "인텐트 서비스 시작"
        }

        button3.text = if(isForgroundServiceRunning) "포그라운드 서비스 중지" else "포그라운드 서비스 시작"
        button3.setOnClickListener {
            if(isForgroundServiceRunning) {
                stopService(forgroundService)
                isForgroundServiceRunning = false
            }
            else {
                if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    startForegroundService(forgroundService)
                }
                else {
                    startService(forgroundService)
                }
                isForgroundServiceRunning = true
            }
            button3.text = if(isForgroundServiceRunning) "포그라운드 서비스 중지" else "포그라운드 서비스 시작"
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        Log.d(LOG_TAG, "${javaClass.name} onDestroy")
    }
}
