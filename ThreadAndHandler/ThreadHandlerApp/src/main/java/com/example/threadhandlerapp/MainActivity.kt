package com.example.threadhandlerapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import kotlinx.android.synthetic.main.activity_main.*


/*
    Main Thread에서의 반복
    - Main Thread에서 처리하는 코드(Activity내의 코드) 중에 일정 작업을 계속 반복 처리해야할 경우가 있다.
    - 이때 무한 루프를 쓰면 화면 처리가 불가능하다.
    - Handler를 통하면 원하는 코드를 반복해서 작업하는 것이 가능하다.


    Handler
    - Handler는 개발자가 안드로이드 OS에게 작업 수행을 요청하는 역할을 한다.
    - 개발자가 작업을 요청하면 안드로이드 OS는 작업을 하지 않을 때 개발가 요청한 작업을 처리하게 된다.
    - 이 처리는 Main Thread에서 처리한다.
    - 5초 이상 걸리는 작업 불가
 */

class MainActivity : AppCompatActivity() {

    private val handler: Handler by lazy {
        Handler()
    }

    var isRunnling = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            var now = System.currentTimeMillis()
            textView1.text = "현재시간 ${now}"
        }

        button2.text = if (isRunnling) "작업중지" else "작업요청"

        button2.setOnClickListener {
            if(isRunnling) {
                isRunnling = false
            }
            else {
                isRunnling = true
                var th = MyThread()
                //handler.post(th)
                handler.postDelayed(th, 100)
            }
            button2.text = if (isRunnling) "작업중지" else "작업요청"
        }
    }


    inner class MyThread: Thread() {
        // 5초 이상 걸리는 작업은 불가.
        override fun run() {
            var time = System.currentTimeMillis()
            textView2.text = "Handler : ${time}"
            if(isRunnling) {
                //SystemClock.sleep(100)
                //handler.post(this)
                handler.postDelayed(this, 100)
            }
        }
    }
}
