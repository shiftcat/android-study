package com.example.boradcast2app

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // com.example.broadcast1app
        // Broadcast1App의 리시버 동작

        button1.setOnClickListener {
            var intent = Intent()
            // Broadcast1App의 Manifest에 정의된 패키지명과 리시버 클래스 명
            // 명시적 인텐트
            intent.setClassName("com.example.broadcast1app", "com.example.broadcast1app.TestReceiver")
            intent.putExtra("data1", 100)
            intent.putExtra("data2", 11.11)
            sendBroadcast(intent)
        }


        /*
            암시적 인텐트로 8.0 이상의 안드로이드에서는 동작하지 않는다.
            안드로이드 8.0 이후 제약사항
            - 안드로이드는 백그라운드 실행이 자유롭다는 점에서 개발의 자율성을 가지지만
               높은 하드웨어 사양을 요구한다는 단점을 가지고 있다.
            - 이에 안드로이드 8.0 이후에는 일부를 제외한
               모든 Broadcast Receiver는 암시적 인텐트로 실행이 불가능해졌다.
         */
        button2.setOnClickListener {
            // Broadcast1App의 리시버 동작
            // 암시적 인텐트
            var intent = Intent("com.example.broadcast.TEST1")
            intent.putExtra("data1", 100)
            intent.putExtra("data2", 11.11)
            sendBroadcast(intent)
        }
    }
}
