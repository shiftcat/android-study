package com.example.threadhandler2app

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.os.SystemClock
import android.util.Log
import kotlinx.android.synthetic.main.activity_main.*


/*
    Handler를 통한 화면 처리
    - 안드로이드에서 네트워크에 관련된 처리나 5초 이상 걸리는 작업은 모두 개발자가 발생하는 쓰래드에서 처리해야 한다.
    - 개발자가 발생하는 쓰래드에서 화면에 관련 처리를 하고자할 때는 Handler를 이용해야 한다.
    - 안드로이드 오레오(8.0) 이상에서는 개발자가 발생한 쓰래드에서 화면처리를 해도 된다.

    Handler
    - 쓰래드에서 코드 처리 중 화면에 관련된 작업이 필요하다면 Handler를 상속받은 클래스를 만들어 필요시 Handler를 요청하면 된다.
    - Handler를 상속 받은 클래스에 만든 메서드는 Main Thread에서 처리한다.

 */
class MainActivity : AppCompatActivity() {


    var isRunnling = false

    lateinit var displayHandler: DisplayHandler


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        displayHandler = DisplayHandler()

        button.setOnClickListener {
            var time = System.currentTimeMillis()
            textView1.text = "현재시간: ${time}"
        }

        isRunnling = true
        var th = MyThread()
        th.start()
    }


    override fun onDestroy() {
        super.onDestroy()
        isRunnling = false
    }



    inner class MyThread: Thread() {
        override fun run() {
            while (isRunnling) {
                SystemClock.sleep(100)
                var time = System.currentTimeMillis()
                Log.d("ThreadHandler2App", "쓰래드: ${time}")
                //textView2.text = "Thread: ${time}"
                // 화면 관려 처리는 handler에 위임 한다.
                //displayHandler.sendEmptyMessage(0)
                var msg = Message()
                msg.what = 0
                msg.obj = time
                displayHandler.sendMessage(msg)
            }
        }
    }


    inner class DisplayHandler: Handler() {
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what) {
                0 -> {
                    textView2.text = "Handler: ${msg?.obj}"
                }
            }
        }
    }
}
