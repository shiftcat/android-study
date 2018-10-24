package com.example.broadcast1app

import android.content.Intent

import android.content.IntentFilter
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addReceiver()

        button.setOnClickListener {
            var intent = Intent(this, TestReceiver::class.java)
            sendBroadcast(intent)
        }
    }



    var testReceiver: TestReceiver? = null


    /*
    8.0 이후 안드로이드에서는 암시적 인텐트로 리시버를 동작시킬 수 없다.
    암시적 인텐트로 리시버를 동작시키기 위해 필요한 것으로
    이 앱이 실행 중일 때만 리시버를 동작시킬 수 있다.

    8.0 이전 안드로이드에서는 작성하지 않아도 된다.
     */
    fun addReceiver()
    {
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return
        }

        testReceiver = TestReceiver()
        var filter = IntentFilter("com.example.broadcast.TEST1")
        registerReceiver(testReceiver, filter)
    }


    override fun onDestroy() {
        super.onDestroy()
        if(testReceiver != null) {
            unregisterReceiver(testReceiver)
            testReceiver = null
        }
    }
}
