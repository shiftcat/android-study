package com.example.toast

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Gravity
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

/*
    Toast
    - 안드로이드에서 간단하게 메시지를 표시할 때 사용한다.
    - 화면과 관련 없이 안드로이드 OS에게 메시지 출력을 요청하고 안드로이드 OS는 각 애플리케이션 요청한 순서대로 메시지를 표시한다.
    - 백그라운드 프로세서의 표시될 수 있다.

    주요 메서드
    makeText: 토스트 메시지 객체를 만든다. 화면 모양, 글자 크기 및 색상 등은 기본 설정되어 있는 것을 사용한다.
    setGravity: 토스트 메시지가 표시될 위치를 설정한다.

 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button1.setOnClickListener {
            var t1 = Toast.makeText(this, "기본 토스트 메시지", Toast.LENGTH_SHORT)
            t1.show()
        }

        button2.setOnClickListener {
            var v1= layoutInflater.inflate(R.layout.custom_toast, null)

            v1?.setBackgroundResource(android.R.drawable.toast_frame)

            // 이미지 변경 시...
            var imgView: ImageView? = v1.findViewById<ImageView>(R.id.imageView)
            imgView?.setImageResource(R.drawable.flag_1)

            var txtView: TextView? = v1.findViewById<TextView>(R.id.textView2)
            txtView?.setTextColor(Color.WHITE)

            var t2 = Toast(this)
            t2.view = v1
            t2.setGravity(Gravity.CENTER, 0, 0)
            t2.show()
        }
    }
}
