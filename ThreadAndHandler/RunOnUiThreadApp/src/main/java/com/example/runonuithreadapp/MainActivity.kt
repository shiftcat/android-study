package com.example.runonuithreadapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    RunOnUIThread
    - RunOnUiThread 메서드는 개발자가 발생시킨 일반 쓰래드에서 코드 일부를 Main Thread 가 처리하도록 하는 메서드이다.
    - Java에서는 메서드로 제공되나 Kotlin에서는 람다식으로 제공되고 있으므로 작성하는 것이 편리하다.

    5초 이상의 작업 또는 네트워크 작업 등...

 */
class MainActivity : AppCompatActivity() {

    var isRunning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var time = System.currentTimeMillis()
            textView1.text = "현재시간: ${time}"
        }

        button2.text = if(isRunning) "작업 중지" else "작업 시작"

        button2.setOnClickListener {
            if(isRunning) {
                isRunning = false
            }
            else {
                isRunning = true
                var th = MyThread()
                th.start()
            }
            button2.text = if(isRunning) "작업 중지" else "작업 시작"
        }
    }


    // 앱을 종료하여도 백그라운드에서 쓰래드가 계속 동작하므로
    // 쓰래드가 반드시 종료될 수 있도록 해야 한다.
    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
    }


    inner class MyThread: Thread() {
        override fun run() {
            while (isRunning) {
                SystemClock.sleep(100)
                var time = System.currentTimeMillis()
                Log.d("RunOnUiThreadApp", "Thread: ${time}")

                // Main Thread에 의해 처리리
                runOnUiThread{
                    textView2.text = "쓰래드: ${time}"
                }
            }
        }
    }
}
