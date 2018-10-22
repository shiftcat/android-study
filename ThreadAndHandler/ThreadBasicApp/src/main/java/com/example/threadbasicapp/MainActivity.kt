package com.example.threadbasicapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    ANR
    - 안드로이드는 Activity의 코드를 처리하기 우해 쓰래드를 발생한다.
      여기서 발생되는 쓰래드를 Main Thread라고 부르며 UI Thread라고 부르기도 한다.
    - Main Thread가 현재 작업을 하지 않을 때만 화면 작업이 가능하며 Main Thread가 바쁠 때 화면 작업이니
      터치가 발생하면 ANR(Application Not Response)가 발생한다.

      주의) 이 예제 프로그램은 안드로이드 8.0(오레오) 이상에서 실행 가능하다.
      8.0 이상에서는 개발자가 작성한 쓰레드에서 UI 관련 처리가 가능하지만 이전 버전은 불가능 하다.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var now = System.currentTimeMillis()
            textView1.text = "현재 시간 ${now}"
        }
        isRunning = true
        var th = MyThread()
        th.start()
    }


    private var isRunning = false


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
                var now = System.currentTimeMillis()
                textView2.text = "Thread: ${now}"
                Log.d("ThreadBasicApp", "쓰레드: ${now}")
            }
        }
    }
}
